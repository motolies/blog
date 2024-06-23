package kr.hvy.blog.infra.scheduler;

import io.github.motolies.scheduler.AbstractScheduler;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import kr.hvy.blog.infra.notify.SlackChannelType;
import kr.hvy.blog.infra.notify.SlackMessenger;
import kr.hvy.blog.module.ppomppu.PpomppuRepository;
import kr.hvy.blog.module.ppomppu.dto.Ppomppu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PpomppuScheduler extends AbstractScheduler {

  private final RestTemplate restTemplateLogging;
  private final PpomppuRepository ppomppuRepository;

  private final String PPOMPPU_BASE_URL = "https://www.ppomppu.co.kr/zboard/";
  private final String PPOMPPU_BOARD_URL = PPOMPPU_BASE_URL + "zboard.php?id=ppomppu&page={0}";


  @Scheduled(cron = "${scheduler.ppomppu.cron-expression}")    // 10분마다
  @SchedulerLock(name = "${scheduler.ppomppu.lock-name}", lockAtLeastFor = "PT30S", lockAtMostFor = "PT1M")
  public void monitoring() {

    proceedScheduler("PPOMPPU")
        .accept(this::ppomppu);
  }

  private void ppomppu() {
    try {
      List<Ppomppu> list = IntStream.range(1, 4)
          .mapToObj(i -> {
            String url = MessageFormat.format(PPOMPPU_BOARD_URL, i);
            String response = restTemplateLogging.getForObject(url, String.class);
            return pageProcess(response);
          })
          .flatMap(List::stream)
          .filter(Objects::nonNull)
          .filter(ppomppuProduct -> ppomppuProduct.getView() > 4000
              || (ppomppuProduct.getRecommendUp() > 4 && ppomppuProduct.getRecommendDown() + 2 < ppomppuProduct.getRecommendUp())
              || ppomppuProduct.getComment() > 25)
          .toList();

      List<String> seqList = list.stream().map(p -> String.valueOf(p.getSeq())).toList();
      List<Ppomppu> savedList = (List<Ppomppu>) ppomppuRepository.findAllById(seqList);

      List<Ppomppu> sendList = list.stream()
          .filter(ppomppu -> savedList.stream().noneMatch(saved -> saved.getSeq() == ppomppu.getSeq()))
          .peek(ppomppu -> {
            SlackMessenger.send(SlackChannelType.HVY_HOT_DEAL, ppomppu.getSlackMessage(), false);
            ppomppuRepository.save(ppomppu);
          }).toList();


    } catch (Exception e) {
      SlackMessenger.send(e);
      log.error(e.getMessage(), e);
    }
  }

  private List<Ppomppu> pageProcess(String content) {
    Document document = Jsoup.parse(content);
    // 광고를 거르기 위해서 게시물 번호 부분에 img 태그가 없는 tr 태그만 추출
    Elements noImageTds = document.select("#revolution_main_table > tbody > tr > td.baseList-space.baseList-numb:not(:has(img))");

    // 이미지가 없는 td 요소의 부모 tr 요소 리스트 가져오기
    List<Element> trElements = noImageTds.stream()
        .map(Element::parent)
        .toList();

    return trElements.stream()
        .map(this::parseElement)
        .filter(Objects::nonNull)
        .toList();
  }

  private Ppomppu parseElement(org.jsoup.nodes.Element element) {

    // 제목에서 품절여부 체크
    Element seqElements = element.select(".baseList-title span").first();
    if (ObjectUtils.isEmpty(seqElements) || StringUtils.isBlank(seqElements.text())) {
      return null;
    }

    return Ppomppu.builder()
        .seq(Integer.parseInt(element.selectFirst(".baseList-numb").text()))
        .title(seqElements.text())
        .link(getLink(element))
        .view(Integer.parseInt(element.selectFirst(".baseList-views").text()))
        .recommendUp(getRecommendUp(element))
        .recommendDown(getRecommendDown(element))
        .comment(getComment(element))
        .build();
  }

  private String getLink(org.jsoup.nodes.Element element) {
    String originalUrl = element.selectFirst(".baseList-title").attr("href");

    // URL에서 쿼리 스트링 부분만 추출
    String queryString = originalUrl.substring(originalUrl.indexOf('?') + 1);

    // 쿼리 스트링을 &로 분리하여 Map에 저장
    Map<String, String> params = new HashMap<>();
    for (String param : queryString.split("&")) {
      String[] keyValue = param.split("=");
      String key = keyValue[0];
      String value = keyValue.length > 1 ? keyValue[1] : "";
      params.put(key, value);
    }

    // id와 no 파라미터 값만 추출하여 새로운 URL 생성
    StringBuilder newUrl = new StringBuilder(originalUrl.substring(0, originalUrl.indexOf('?') + 1));
    newUrl.append("id=").append(params.get("id"));
    if (params.containsKey("no")) {
      newUrl.append("&no=").append(params.get("no"));
    }

    return PPOMPPU_BASE_URL + newUrl.toString();
  }


  private int getRecommendUp(org.jsoup.nodes.Element element) {
    Element recommend = element.selectFirst(".baseList-rec");
    if (Objects.isNull(recommend) || StringUtils.isBlank(recommend.text())) {
      return 0;
    } else {
      if (recommend.text().contains("-")) {
        return Integer.parseInt(recommend.text().split("-")[0].trim());
      } else {
        return 0;
      }
    }
  }

  private int getRecommendDown(org.jsoup.nodes.Element element) {
    Element recommend = element.selectFirst(".baseList-rec");
    if (Objects.isNull(recommend) || StringUtils.isBlank(recommend.text())) {
      return 0;
    } else {
      if (recommend.text().contains("-")) {
        return Integer.parseInt(recommend.text().split("-")[1].trim());
      } else {
        return 0;
      }
    }
  }

  private int getComment(org.jsoup.nodes.Element element) {
    Element comment = element.selectFirst(".baseList-c");
    if (Objects.isNull(comment) || StringUtils.isBlank(comment.text())) {
      return 0;
    } else {
      return Integer.parseInt(comment.text());
    }
  }


}

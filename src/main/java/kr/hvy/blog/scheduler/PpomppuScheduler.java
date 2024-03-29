package kr.hvy.blog.scheduler;

import io.github.motolies.scheduler.AbstractScheduler;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kr.hvy.blog.model.SlackChannelType;
import kr.hvy.blog.util.SlackMessenger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
//@Profile("!default")
public class PpomppuScheduler extends AbstractScheduler {

  private RedisTemplate<String, Ppomppu> redisTemplate;
  private final RestTemplate restTemplateLogging;
  private final PpomppuRepository ppomppuRepository;

  private final String PPOMPPU_BASE_URL = "https://www.ppomppu.co.kr/zboard/";
  private final String PPOMPPU_BOARD_URL = PPOMPPU_BASE_URL + "zboard.php?id=ppomppu&page={0}";


  @Scheduled(cron = "${scheduler.ppomppu.cron-expression}")    // 10분마다
  @SchedulerLock(name = "${scheduler.ppomppu.lock-name}", lockAtLeastFor = "PT5M", lockAtMostFor = "PT9M")
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

  public List<Ppomppu> findBySeqIn(List<Integer> seqs) {
    String[] keys = seqs.stream()
        .map(seq -> "ppomppuN:" + seq)
        .toArray(String[]::new);
    List<Ppomppu> result = redisTemplate.opsForValue().multiGet(Arrays.asList(keys))
        .stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    return result;
  }

  private List<Ppomppu> pageProcess(String content) {
    Document document = Jsoup.parse(content);
    Elements trElements = document.select("table#revolution_main_table tbody tr.common-list0,table#revolution_main_table tbody tr.common-list1");

    return trElements.stream()
        .map(this::parseElement)
        .filter(Objects::nonNull)
        .toList();
  }

  private Ppomppu parseElement(org.jsoup.nodes.Element element) {

    // 제목에서 품절여부 체크
    Elements titles = element.select("font.list_title");
    Element seqElements = element.select("td.eng.list_vspace[colspan=2]").first();
    if (titles.isEmpty() || ObjectUtils.isEmpty(seqElements) || StringUtils.isBlank(seqElements.text())) {
      return null;
    }

    return Ppomppu.builder()
        .seq(Integer.parseInt(seqElements.text()))
        .title(Objects.requireNonNull(titles.first()).text())
        .link(PPOMPPU_BASE_URL + Objects.requireNonNull(element.select("a.baseList-title").first()).attr("href"))
        .view(Integer.parseInt(element.select("td.eng.list_vspace[colspan=2]").get(3).text()))
        .recommendUp(getRecommendUp(element))
        .recommendDown(getRecommendDown(element))
        .comment(getComment(element))
        .build();
  }

  private int getRecommendUp(org.jsoup.nodes.Element element) {
    String recommend = element.select("td.eng.list_vspace[colspan=2]").get(2).text().trim();
    if (recommend.contains("-")) {
      return Integer.parseInt(recommend.split("-")[0].trim());
    } else {
      return 0;
    }
  }

  private int getRecommendDown(org.jsoup.nodes.Element element) {
    String recommend = element.select("td.eng.list_vspace[colspan=2]").get(2).text().trim();
    if (recommend.contains("-")) {
      return Integer.parseInt(recommend.split("-")[0].trim());
    } else {
      return 0;
    }
  }

  private int getComment(org.jsoup.nodes.Element element) {
    Elements spans = element.select("span[onclick*=win_comment]");
    if (spans.isEmpty()) {
      return 0;
    } else {
      return Integer.parseInt(Objects.requireNonNull(spans.first()).text());
    }
  }


}

package kr.hvy.blog.module.discount.site;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import kr.hvy.blog.module.discount.AbstractDiscountHandler;
import kr.hvy.blog.module.discount.DiscountService;
import kr.hvy.blog.module.discount.code.DiscountType;
import kr.hvy.blog.module.discount.dto.DiscountInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PpomppuHandler extends AbstractDiscountHandler {

  private final String BASE_URL = "https://www.ppomppu.co.kr/zboard/";
  private final String BOARD_URL = BASE_URL + "zboard.php?id=ppomppu&page={0}";

  protected PpomppuHandler(RestTemplate restTemplateLogging, DiscountService discountService) {
    super(restTemplateLogging, discountService);
  }

  @Override
  public List<DiscountInfo> getList() {
    return IntStream.range(1, 4)
        .mapToObj(i -> {
          String url = MessageFormat.format(BOARD_URL, i);
          String response = restTemplateLogging.getForObject(url, String.class);
          return pageProcess(response);
        })
        .flatMap(List::stream)
        .filter(Objects::nonNull)
        .toList();
  }

  @Override
  public Predicate<DiscountInfo> filtering() {
    return discount -> discount.getView() > 4000
        || (discount.getRecommendUp() > 4 && discount.getRecommendDown() + 2 < discount.getRecommendUp())
        || discount.getComment() > 25;
  }


  private List<DiscountInfo> pageProcess(String content) {
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

  private DiscountInfo parseElement(org.jsoup.nodes.Element element) {

    // 제목에서 품절여부 체크
    Element seqElements = element.select(".baseList-title span").first();
    if (ObjectUtils.isEmpty(seqElements) || StringUtils.isBlank(seqElements.text())) {
      return null;
    }

    return DiscountInfo.builder()
        .redisKey(Integer.parseInt(element.selectFirst(".baseList-numb").text()))
        .type(DiscountType.PPOMPPU)
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

    return BASE_URL + newUrl.toString();
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

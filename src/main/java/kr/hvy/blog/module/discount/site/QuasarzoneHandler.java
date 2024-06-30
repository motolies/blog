package kr.hvy.blog.module.discount.site;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import kr.hvy.blog.module.discount.AbstractDiscountHandler;
import kr.hvy.blog.module.discount.DiscountService;
import kr.hvy.blog.module.discount.code.DiscountType;
import kr.hvy.blog.module.discount.dto.DiscountInfo;
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
public class QuasarzoneHandler extends AbstractDiscountHandler {

  private final String BASE_URL = "https://quasarzone.com";
  private final String BASE_BOARD_URL = BASE_URL + "/bbs/qb_saleinfo";
  private final String BOARD_URL = BASE_BOARD_URL + "?page={0}";
  private final String DETAIL_URL = BASE_BOARD_URL + "/views/{0}";

  private final Pattern ID_PATTERN = Pattern.compile("views/(\\d+)");


  protected QuasarzoneHandler(RestTemplate restTemplateLogging, DiscountService discountService) {
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
    return discount -> discount.getView() > 5000
        || discount.getRecommendUp() > 5
        || discount.getComment() > 25;
  }


  private List<DiscountInfo> pageProcess(String content) {
    Document document = Jsoup.parse(content);
    // 광고를 거르기 위해서 게시물 번호 부분에 img 태그가 없는 tr 태그만 추출
    Elements tr = document.select("div.market-type-list > table > tbody > tr");

    return tr.stream()
        .map(this::parseElement)
        .filter(Objects::nonNull)
        .toList();
  }

  private String getTrId(Element link) {
    String trId = link.attribute("href").toString();
    Matcher matcher = ID_PATTERN.matcher(trId);
    if (matcher.find()) {
      return matcher.group(1);
    } else {
      return null;
    }
  }

  private DiscountInfo parseElement(Element element) {
    Element link = element.selectFirst("a.subject-link");

    if (link == null) {
      return null;
    }

    String linkNo = getTrId(link);
    String title = Optional.ofNullable(link.selectFirst("span.ellipsis-with-reply-cnt"))
        .map(t -> t.text().trim())
        .orElse("");
    String price = Optional.ofNullable(element.selectFirst("span.text-orange"))
        .map(span -> span.text().trim())
        .orElse("");

    return DiscountInfo.builder()
        .redisKey(Integer.parseInt(linkNo))
        .type(DiscountType.QUASARZONE)
        .title(MessageFormat.format("{0} ({1})", title, price))
        .link(getLink(linkNo))
        .view(Integer.parseInt(getCount(element)))
        .recommendUp(getRecommendUp(element))
        .comment(getComment(element))
        .build();
  }

  private String getCount(Element element) {
    Element spanTag = element.selectFirst("span.count");
    return Optional.ofNullable(spanTag)
        .map(span -> {
          String text = span.text().trim();
          if (text.contains("k")) {
            text = text.replace("k", "000")
                .replace(".", "");
          }
          return text;
        })
        .orElse("0");
  }

  private String getLink(String linkNo) {
    return MessageFormat.format(DETAIL_URL, linkNo);
  }


  private int getRecommendUp(Element element) {
    String recommendUp = Optional.ofNullable(element.selectFirst("td"))
        .map(td -> td.selectFirst("span.num").text().trim())
        .orElse("0");
    return Integer.parseInt(recommendUp);
  }


  private int getComment(Element element) {
    String commentCount = Optional.ofNullable(element.selectFirst("span.ctn-count"))
        .map(span -> span.text().trim())
        .orElse("0");
    return Integer.parseInt(commentCount);
  }

}

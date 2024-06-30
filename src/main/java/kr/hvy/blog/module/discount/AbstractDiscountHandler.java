package kr.hvy.blog.module.discount;

import java.util.List;
import java.util.function.Predicate;
import kr.hvy.blog.infra.exception.AuthenticationException;
import kr.hvy.blog.infra.notify.SlackChannelType;
import kr.hvy.blog.infra.notify.SlackMessenger;
import kr.hvy.blog.module.discount.dto.Discount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class AbstractDiscountHandler {

  @Autowired
  protected RestTemplate restTemplateLogging;

  @Autowired
  private DiscountService discountService;

  protected AbstractDiscountHandler(RestTemplate restTemplateLogging, DiscountService discountService) {
    this.restTemplateLogging = restTemplateLogging;
    this.discountService = discountService;
  }

  protected boolean authorize() {
    return true;
  }

  protected abstract List<Discount> getList();

  protected abstract Predicate<Discount> filtering();


  public void run() {
    try {

      if (!authorize()) {
        throw new AuthenticationException("인증정보가 없습니다.");
      }

      List<Discount> discountList = getList().stream()
          .filter(filtering())
          .toList();

      List<String> seqList = discountList.stream().map(p -> String.valueOf(p.getRedisKey())).toList();
      List<Discount> savedList = discountService.savedDiscounts(seqList);

      discountList.stream()
          .filter(discount -> savedList.stream().noneMatch(saved -> saved.getRedisKey() == discount.getRedisKey()))
          .peek(discount -> {
            SlackMessenger.send(SlackChannelType.HVY_HOT_DEAL, discount.getSlackMessage(), false);
            discountService.save(discount);
          }).toList();

    } catch (Exception e) {
      SlackMessenger.send(e);
      log.error(e.getMessage(), e);
    }

  }

}

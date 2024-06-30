package kr.hvy.blog.module.discount;

import java.util.List;
import kr.hvy.blog.infra.exception.AuthenticationException;
import kr.hvy.blog.infra.notify.SlackChannelType;
import kr.hvy.blog.infra.notify.SlackMessenger;
import kr.hvy.blog.module.discount.dto.DiscountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class AbstractDiscountHandler implements DiscountInterface {

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

  @Async
  public void run() {
    try {

      if (!authorize()) {
        throw new AuthenticationException("인증정보가 없습니다.");
      }

      List<DiscountInfo> discountList = getList().stream()
          .filter(filtering())
          .toList();

      List<String> seqList = discountList.stream().map(p -> String.valueOf(p.getRedisKey())).toList();
      List<DiscountInfo> savedList = discountService.savedDiscounts(seqList);

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

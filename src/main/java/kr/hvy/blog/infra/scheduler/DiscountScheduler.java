package kr.hvy.blog.infra.scheduler;

import io.github.motolies.scheduler.AbstractScheduler;
import java.util.Arrays;
import kr.hvy.blog.infra.core.BeanHandler;
import kr.hvy.blog.module.discount.DiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class DiscountScheduler extends AbstractScheduler {

  private final BeanHandler beanHandler;

  @Scheduled(cron = "${scheduler.ppomppu.cron-expression}")    // 10분마다
  @SchedulerLock(name = "${scheduler.ppomppu.lock-name}", lockAtLeastFor = "PT30S", lockAtMostFor = "PT1M")
  public void monitoring() {

    // DiscountType의 enum 만큼 동작시킨다.
    Arrays.stream(DiscountType.values())
        .peek(type -> {
          beanHandler.getHandler(type.getHandler())
              .ifPresent(handler -> {
                proceedScheduler(type.getDesc())
                    .accept(handler::run);
              });
        })
        .toList();

  }
}

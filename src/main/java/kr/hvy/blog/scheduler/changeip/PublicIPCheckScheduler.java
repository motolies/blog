package kr.hvy.blog.scheduler.changeip;

import io.github.motolies.scheduler.AbstractScheduler;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import kr.hvy.blog.model.SlackChannelType;
import kr.hvy.blog.util.SlackMessenger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicIPCheckScheduler extends AbstractScheduler {

  private final RestTemplate restTemplateLogging;
  private static final String AWS_IP_CHECK_URL = "https://checkip.amazonaws.com";
  private final PublicIPRepository publicIPRepository;
  private static final String REDIS_PUBLIC_IP_KEY = "CURRENT_PUBLIC_IP";

  @Scheduled(cron = "${scheduler.public-ip.cron-expression}")    // 10분마다
  @SchedulerLock(name = "${scheduler.public-ip.lock-name}", lockAtLeastFor = "PT30S", lockAtMostFor = "PT50S")
  public void monitoring() {
    proceedScheduler("PUBLIC-IP-CHANGE")
        .accept(this::checkPublicIp);
  }

  private void checkPublicIp() {
    try {

      // 현재 퍼블릭 IP를 AWS를 통해 확인
      String newPublicIP = getPublicIPFromAWS();

      // 현재 저장된 public ip 체크
      // 저장된 값이 없으면 저장하고 종료
      // 저장된 값이 있으면 비교하여 다르면 알림
      Optional<PublicIP> oldPublicIP = publicIPRepository.findById(REDIS_PUBLIC_IP_KEY);
      oldPublicIP.ifPresentOrElse(old -> {
            if (!old.getIp().equals(newPublicIP)) {
              String msg = "Public IP changed from " + old.getIp() + " to " + newPublicIP;
              log.error(msg);
              SlackMessenger.send(SlackChannelType.HVY_ERROR, msg, true);

              // update new ip
              old.setIp(newPublicIP);
              publicIPRepository.save(old);
            }
          },
          () -> {
            publicIPRepository.save(PublicIP.builder().id(REDIS_PUBLIC_IP_KEY).ip(newPublicIP).build());
          });

    } catch (IOException | InterruptedException e) {
      log.error("Error checking public IP", e);
    }
  }

  private String getPublicIPFromAWS() throws IOException, InterruptedException {
    return Objects.requireNonNull(restTemplateLogging.getForObject(AWS_IP_CHECK_URL, String.class)).trim();
  }


}

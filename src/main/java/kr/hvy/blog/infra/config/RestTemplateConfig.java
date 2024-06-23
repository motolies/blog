package kr.hvy.blog.infra.config;

import io.github.motolies.code.RestTemplateConfigType;
import io.github.motolies.config.RestTemplateConfigurer;
import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final RestTemplateConfigurer restTemplateConfigurer;

  @Bean
  public RestTemplate restTemplate() {
    return restTemplateConfigurer.restTemplate();
  }

  @Bean("restTemplateLogging")
  public RestTemplate restTemplateLogging() {
    return restTemplateConfigurer.restTemplate(30, 30, EnumSet.of(RestTemplateConfigType.LOGGING, RestTemplateConfigType.USER_AGENT));
  }


}

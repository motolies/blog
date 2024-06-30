package kr.hvy.blog.infra.core;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeanHandler {

  private final ApplicationContext applicationContext;

  public <T> Optional<T> getHandler(Class<T> clazz) {
    try {
      return Optional.of(applicationContext.getBean(clazz));
    } catch (Exception e) {
      log.error("Not found Bean. clazz : {}", clazz, e);
      return Optional.empty();
    }
  }

  public <T> Optional<T> getHandler(String name, Class<T> clazz) {
    if (applicationContext.containsBean(name)) {
      return Optional.of(applicationContext.getBean(name, clazz));
    }
    return Optional.empty();
  }


}

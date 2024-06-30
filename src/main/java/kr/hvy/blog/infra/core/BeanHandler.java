package kr.hvy.blog.infra.core;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeanHandler {

  private final ApplicationContext applicationContext;

  public <T> Optional<T> getHandler(Class<T> clazz) {
    return getHandler(null, clazz);
  }

  public <T> Optional<T> getHandler(String name, Class<T> clazz) {
    try {
      name = ObjectUtils.defaultIfNull(name, StringUtils.uncapitalize(clazz.getSimpleName()));
      if (applicationContext.containsBean(name)) {
        Object bean = applicationContext.getBean(name);
        // 프록시를 처리하여 실제 빈 클래스를 가져옵니다.
        // 해당 빈 클래스에 아래 주석이 필요합니다.
        // @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (clazz.isAssignableFrom(targetClass)) {
          return Optional.of(clazz.cast(bean));
        }
      }
    } catch (Exception e) {
      log.error("Not found Bean. clazz : {}", clazz, e);
    }
    return Optional.empty();
  }


}

package kr.hvy.blog.infra.security;

import com.google.common.net.InternetDomainName;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieProvider {

  //https://velog.io/@kwakwoohyun/%EC%9D%B4%EC%8A%88%EC%B2%98%EB%A6%AC-spring-boot-local-cookie
  private static final String COOKIE_PATH = "/";
  private static final boolean READONLY = true;
  private static int MAX_AGE;

  @Value("${jwt.expiration}")
  public void setMAX_AGE(String age) {
    this.MAX_AGE = Integer.parseInt(age);
  }

  private static String getRootDomain(String refererUrl) {
    try {
      URI uri = new URI(refererUrl);
      return InternetDomainName.from(uri.getHost()).topDomainUnderRegistrySuffix().toString();
    } catch (Exception e) {
      log.error("getRootDomain error : {}", e.getMessage());
      return null;
    }
  }

  public static ResponseCookie setSpringCookie(HttpServletRequest request, String name, String value) {
    String domain = getRootDomain(request.getHeader("referer"));
    if (domain != null) {
      return ResponseCookie.from(name, value)
          .sameSite("None")
          .secure(true)
          .domain(domain)
          .path(COOKIE_PATH)
          .httpOnly(READONLY)
          .maxAge(MAX_AGE)
          .build();
    } else {
      return ResponseCookie.from(name, value)
          .secure(false)
          .path(COOKIE_PATH)
          .httpOnly(READONLY)
          .maxAge(MAX_AGE)
          .build();
    }
  }

  public static ResponseCookie removeSpringCookie(HttpServletRequest request, String name) {
    String domain = getRootDomain(request.getHeader("referer"));

    if (domain != null) {
      return ResponseCookie.from(name, null)
          .sameSite("None")
          .secure(true)
          .domain(domain)
          .path(COOKIE_PATH)
          .httpOnly(READONLY)
          .maxAge(0)
          .build();
    } else {
      return ResponseCookie.from(name, null)
          .secure(false)
          .path(COOKIE_PATH)
          .httpOnly(READONLY)
          .maxAge(0)
          .build();
    }
  }

}

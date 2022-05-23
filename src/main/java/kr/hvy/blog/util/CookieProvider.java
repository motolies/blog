package kr.hvy.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class CookieProvider {
    //https://velog.io/@kwakwoohyun/%EC%9D%B4%EC%8A%88%EC%B2%98%EB%A6%AC-spring-boot-local-cookie
    private static final String DOMAIN = "hvy.kr";
    private static final String COOKIE_PATH = "/";
    private static final boolean READONLY = true;
    private static int MAX_AGE;

    @Value("${jwt.expiration}")
    public void setMAX_AGE(String age) {
        this.MAX_AGE = Integer.parseInt(age);
    }

    public static ResponseCookie setSpringCookie(HttpServletRequest request, String name, String value) {
        if (request.getHeader("referer").contains(DOMAIN)) {
            return ResponseCookie.from(name, value)
                    .sameSite("None")
                    .secure(true)
                    .domain(DOMAIN)
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
        if (request.getHeader("referer").contains(DOMAIN)) {
            return ResponseCookie.from(name, null)
                    .sameSite("None")
                    .secure(true)
                    .domain(DOMAIN)
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

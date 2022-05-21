package kr.hvy.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {
    //https://velog.io/@kwakwoohyun/%EC%9D%B4%EC%8A%88%EC%B2%98%EB%A6%AC-spring-boot-local-cookie
    private static final String DOMAIN = "hvy.kr";
    private static final String COOKIE_PATH = "/";
    private static final boolean USE_HTTPS = true;
    private static final boolean READONLY = true;
    private static int MAX_AGE;

    @Value("${jwt.expiration}")
    public void setMAX_AGE(String age) {
        this.MAX_AGE = Integer.parseInt(age);
    }

    public static ResponseCookie setSpringCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(MAX_AGE)
                .domain(DOMAIN)
                .build();
    }

    public static ResponseCookie removeSpringCookie(String name) {
        return ResponseCookie.from(name, null)
                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(0)
                .domain(DOMAIN)
                .build();
    }

}

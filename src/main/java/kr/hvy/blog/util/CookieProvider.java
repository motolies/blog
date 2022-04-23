package kr.hvy.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    private static final String COOKIE_PATH = "/";
    private static final boolean USE_HTTPS = false;
    private static final boolean READONLY = true;
    private static int MAX_AGE;

    @Value("${jwt.expiration}")
    public void setMAX_AGE(String age) {
        this.MAX_AGE = Integer.parseInt(age);
    }

    public static ResponseCookie setSpringCookie(String name, String value) {
        return ResponseCookie.from(name, value)
//                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(MAX_AGE)
                .build();
    }

    public static ResponseCookie removeSpringCookie(String name) {
        return ResponseCookie.from(name, null)
//                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(0)
                .build();
    }

}

package kr.hvy.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(MAX_AGE)
                .build();
    }

    public static ResponseCookie removeSpringCookie(String name) {
        return ResponseCookie.from(name, null)
                .sameSite("None")
                .path(COOKIE_PATH)
                .secure(USE_HTTPS)
                .httpOnly(READONLY)
                .maxAge(0)
                .build();
    }


    public static void extendExpirationTime(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                insert(response, cookies[i].getName(), cookies[i].getValue());
            }
        }
    }

    public static String findByName(HttpServletRequest request, String key) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if ((cookies[i].getName()).equals(key)) {
                    value = cookies[i].getValue();
                }
            }
        }
        return value;
    }

    public static void insert(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value); // 쿠키 생성
        cookie.setHttpOnly(READONLY); // js에서 쿠키를 읽지 못하도록
        cookie.setSecure(USE_HTTPS); // https 에서만 쿠키를 전송하도록
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(MAX_AGE); // 기간을 하루로 지정 - 일주일로 변경
        response.addCookie(cookie);
    }

    public static void insertToken(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value); // 쿠키 생성
        cookie.setHttpOnly(READONLY); // js에서 쿠키를 읽지 못하도록
        cookie.setSecure(USE_HTTPS); // https 에서만 쿠키를 전송하도록
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(MAX_AGE); // 기간을 하루로 지정 - 일주일로 변경
        response.addCookie(cookie);
    }

    public static void delete(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if ((cookies[i].getName()).equals(key)) {
                    cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                    cookies[i].setHttpOnly(READONLY); // js에서 쿠키를 읽지 못하도록
                    cookies[i].setSecure(USE_HTTPS); // https 에서만 쿠키를 전송하도록
                    cookies[i].setPath(COOKIE_PATH);
                    response.addCookie(cookies[i]); // 응답 헤더에 추가
                }

            }
        }
    }

    public static void deleteAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                cookies[i].setHttpOnly(READONLY); // js에서 쿠키를 읽지 못하도록
                cookies[i].setSecure(USE_HTTPS); // https 에서만 쿠키를 전송하도록
                cookies[i].setPath(COOKIE_PATH);
                response.addCookie(cookies[i]); // 응답 헤더에 추가
            }
        }
    }

}

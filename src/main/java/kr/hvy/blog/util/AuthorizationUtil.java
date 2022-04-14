package kr.hvy.blog.util;

import kr.hvy.blog.security.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthorizationUtil {

    public static boolean hasAdminRole() {
        //https://jason-moon.tistory.com/132
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
    }

    public static byte[] getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtUser)authentication.getPrincipal()).getId();
    }


}

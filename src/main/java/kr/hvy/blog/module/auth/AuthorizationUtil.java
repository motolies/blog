package kr.hvy.blog.module.auth;

import kr.hvy.blog.infra.security.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthorizationUtil {

    public static boolean hasAdminRole() {
        //https://jason-moon.tistory.com/132
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(o -> o.getAuthority().equals("ROLE_ADMIN"));
    }

    public static byte[] getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtUser)authentication.getPrincipal()).getId();
    }


}

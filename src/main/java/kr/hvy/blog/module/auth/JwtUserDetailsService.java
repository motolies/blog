package kr.hvy.blog.module.auth;


import kr.hvy.blog.infra.exception.AuthenticationException;
import kr.hvy.blog.module.auth.domain.Authority;
import kr.hvy.blog.module.auth.domain.AuthorityName;
import kr.hvy.blog.module.auth.domain.User;
import kr.hvy.blog.infra.security.JwtUserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthorityService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));

        if (user.getAuthority().stream().noneMatch(a -> a.getName() == AuthorityName.ROLE_USER)) {
            Authority userAuth = authService.findByName(AuthorityName.ROLE_USER);
            Set<Authority> auths = user.getAuthority();
            auths.add(userAuth);
            user.setAuthority(auths);
            user.getAuthority().add(userAuth);
        }

       if (!user.getEnabled()) {
            throw new AuthenticationException(String.format("'%s' is disabled.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }

}

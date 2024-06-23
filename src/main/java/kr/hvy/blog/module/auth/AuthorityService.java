package kr.hvy.blog.module.auth;

import kr.hvy.blog.module.auth.domain.Authority;
import kr.hvy.blog.module.auth.domain.AuthorityName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority findByName(AuthorityName name) {
        return authorityRepository.findByName(name);
    }

}

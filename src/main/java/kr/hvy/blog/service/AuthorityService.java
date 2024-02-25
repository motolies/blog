package kr.hvy.blog.service;

import kr.hvy.blog.entity.Authority;
import kr.hvy.blog.entity.AuthorityName;
import kr.hvy.blog.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final EntityManager em;

    private final AuthorityRepository authorityRepository;

    public Authority findByName(AuthorityName name) {
        return authorityRepository.findByName(name);
    }

}

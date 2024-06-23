package kr.hvy.blog.module.auth;

import kr.hvy.blog.module.auth.domain.Authority;
import kr.hvy.blog.module.auth.domain.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthorityRepository extends JpaRepository<Authority, byte[]> {
    Authority findByName(AuthorityName name);
}

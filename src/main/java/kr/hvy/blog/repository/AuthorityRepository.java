package kr.hvy.blog.repository;

import kr.hvy.blog.model.Authority;
import kr.hvy.blog.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, byte[]> {
    Authority findByName(AuthorityName name);
}

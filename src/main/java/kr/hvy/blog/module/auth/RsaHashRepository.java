package kr.hvy.blog.module.auth;

import kr.hvy.blog.module.auth.domain.RsaHash;
import org.springframework.data.repository.CrudRepository;

public interface RsaHashRepository extends CrudRepository<RsaHash, String> {
}

package kr.hvy.blog.repository;

import kr.hvy.blog.entity.redis.RsaHash;
import org.springframework.data.repository.CrudRepository;

public interface RsaHashRepository extends CrudRepository<RsaHash, String> {
}

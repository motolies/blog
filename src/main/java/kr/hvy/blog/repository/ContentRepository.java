package kr.hvy.blog.repository;

import kr.hvy.blog.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {

    Content findByIsMainTrue();

    Content findBySyncKey(String synckey);
}

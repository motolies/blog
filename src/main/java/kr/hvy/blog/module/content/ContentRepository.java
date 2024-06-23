package kr.hvy.blog.module.content;

import kr.hvy.blog.module.content.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {

    Content findByIsMainTrue();

    Content findBySyncKey(String synckey);

    List<Content> findContentsByIdInOrderByCreateDateDesc(List<Integer> ids);

}

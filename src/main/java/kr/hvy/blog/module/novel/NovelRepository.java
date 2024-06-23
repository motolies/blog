package kr.hvy.blog.module.novel;

import kr.hvy.blog.module.novel.domain.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {

    List<Novel> findByTitleOrderBySeq(String title);
    Optional<Novel> findByTitleAndSeq(String title, Integer seq);

}

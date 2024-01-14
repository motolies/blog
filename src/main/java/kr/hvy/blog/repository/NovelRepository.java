package kr.hvy.blog.repository;

import kr.hvy.blog.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {

    List<Novel> findByTitleOrderBySeq(String title);
    Optional<Novel> findByTitleAndSeq(String title, Integer seq);

}

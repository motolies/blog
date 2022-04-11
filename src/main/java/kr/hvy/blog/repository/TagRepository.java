package kr.hvy.blog.repository;

import kr.hvy.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag> findByNameContaining(String name);

}

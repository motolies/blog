package kr.hvy.blog.repository;

import kr.hvy.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Set<Tag> findByNameContainingOrderByName(String name);

    Set<Tag> findByIdIn(Set<Integer> ids);

    Set<Tag> findAllByOrderByName();

    Tag findByName(String name);


}

package kr.hvy.blog.module.tag;

import kr.hvy.blog.module.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Set<Tag> findByNameContainingOrderByName(String name);

    Set<Tag> findByIdIn(Set<Integer> ids);

    Set<Tag> findAllByOrderByName();

    Tag findByName(String name);


}

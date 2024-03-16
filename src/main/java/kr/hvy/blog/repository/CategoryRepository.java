package kr.hvy.blog.repository;

import java.util.Optional;
import kr.hvy.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}

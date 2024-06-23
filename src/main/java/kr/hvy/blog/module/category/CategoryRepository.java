package kr.hvy.blog.module.category;

import kr.hvy.blog.module.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}

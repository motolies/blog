package kr.hvy.blog.repository;

import kr.hvy.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @SuppressWarnings({"unchecked", "JpaQlInspection"})

    @Query("select c from Category as c where PId is null")
    Category findRoot();


}

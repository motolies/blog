package kr.hvy.blog.service;

import kr.hvy.blog.model.Category;
import kr.hvy.blog.util.MultipleResultSet;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {

    MultipleResultSet findCategoryWithProc() throws SQLException;

    Category findById(String id);

    List<Category> findAll();

    Category findRoot();

    void saveWithProc(Category cat) throws SQLException;

    void updateFullName() throws SQLException;

    void deleteWithProc(String categoryId) throws SQLException;


    /**
     * @Deprecated hibernate 로 동작시 원하는 결과물 얻기가 힘들어서 proc 호출로 변경함
     */
    @Deprecated
    void deleteById(String id);

    /**
     * @Deprecated hibernate 로 동작시 원하는 결과물 얻기가 힘들어서 proc 호출로 변경함
     */
    @Deprecated
    List<Category> saveAll(List<Category> entities);

    /**
     * @Deprecated hibernate 로 동작시 원하는 결과물 얻기가 힘들어서 proc 호출로 변경함
     */
    @Deprecated
    Category save(Category cat);

}

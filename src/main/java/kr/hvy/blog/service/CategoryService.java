package kr.hvy.blog.service;

import kr.hvy.blog.entity.Category;
import kr.hvy.blog.mapper.CategoryMapper;
import kr.hvy.blog.model.request.CategorySaveDto;
import kr.hvy.blog.model.response.CategoryFlatResponseDto;
import kr.hvy.blog.repository.CategoryRepository;
import kr.hvy.blog.util.MultipleResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    @PersistenceContext
    private EntityManager em;

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;


    public List<CategoryFlatResponseDto> findAllCategory() {
        return categoryMapper.findAllCategory();
    }

    public MultipleResultSet findCategoryWithProc() throws SQLException {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        MultipleResultSet multipleResultSet = new MultipleResultSet();
        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_category_select()}");

            multipleResultSet.processResultSet(callableSt);
            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (!conn.isClosed())
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return multipleResultSet;
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category findRoot() {
        return categoryRepository.findRoot();
    }


    @Deprecated
    @Transactional
    public List<Category> saveAll(List<Category> entities) {
        List<Category> list = categoryRepository.saveAll(entities);
        categoryRepository.flush();
        return list;
    }

    @Transactional
    public Category save(Category cat) {
        return categoryRepository.saveAndFlush(cat);
    }


    @Transactional
    public void saveWithProc(CategorySaveDto categorySaveDto) throws SQLException {
        categoryMapper.saveCategory(categorySaveDto);
        categoryMapper.updateFullName();
    }


    @Transactional
    public void deleteWithProc(String categoryId) throws SQLException {
        categoryMapper.deleteById(categoryId);
    }
}

package kr.hvy.blog.service;

import kr.hvy.blog.entity.Category;
import kr.hvy.blog.mapper.CategoryMapper;
import kr.hvy.blog.model.response.CategoryDto;
import kr.hvy.blog.repository.CategoryRepository;
import kr.hvy.blog.util.MultipleResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    @PersistenceContext
    private EntityManager em;

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;


    public List<CategoryDto> findAllCategory() {
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
    public void saveWithProc(Category cat) throws SQLException {

        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_category_save(?, ?, ?, ?, ?, ?)}");
            callableSt.setString(1, cat.getId());
            callableSt.setString(2, cat.getName());
            callableSt.setInt(3, cat.getOrder());
            callableSt.setString(4, cat.getFullName());
            callableSt.setString(5, cat.getFullPath());
            callableSt.setString(6, cat.getPId());

            callableSt.execute();
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

    }

    @Transactional
    public void deleteWithProc(String categoryId) throws SQLException {

        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_category_delete(?)}");
            callableSt.setString(1, categoryId);
            callableSt.execute();
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

    }

    @Transactional
    public void updateFullName() throws SQLException {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_category_fullname_update()}");
            callableSt.execute();
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
    }

    @Transactional
    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }

}

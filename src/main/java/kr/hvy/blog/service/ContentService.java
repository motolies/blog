package kr.hvy.blog.service;

import kr.hvy.blog.entity.Content;
import kr.hvy.blog.mapper.ContentMapper;
import kr.hvy.blog.mapper.CountMapper;
import kr.hvy.blog.model.base.Page;
import kr.hvy.blog.repository.ContentRepository;
import kr.hvy.blog.util.AuthorizationProvider;
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
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContentService {

    @PersistenceContext
    private EntityManager em;

    private final ContentRepository contentRepository;

    private final CountMapper countMapper;

    private final ContentMapper contentMapper;

    public Content findById(int id) {
        Content content = contentRepository.findById(id).orElse(null);

        if (content == null || (!content.isPublic() && !AuthorizationProvider.hasAdminRole())) {
            content = new Content();
        } else {
            content.setViewCount(content.getViewCount() + 1);
            this.save(content);
        }
        return content;
    }

    public Content findBySyncKey(String synckey) {
        return contentRepository.findBySyncKey(synckey);
    }

    @Transactional
    public void save(Content content) {
        contentRepository.save(content);
    }

    public Content findByMain() {
        Content content = contentRepository.findByIsMainTrue();

        if (content == null || (!content.isPublic() && !AuthorizationProvider.hasAdminRole())) {
            content = new Content();
        } else {
            content.setViewCount(content.getViewCount() + 1);
            this.save(content);
        }
        return content;
    }

    @Transactional
    public void deleteById(int id) {
        contentRepository.deleteById(id);
    }

    public MultipleResultSet findWithProc(String subject, String body, String fileName, String categoryName, String categoryId, String tagName, int page, int pageSize,
                                          String orderStr) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        MultipleResultSet multipleResultSet = new MultipleResultSet(page, pageSize);
        try {
            conn = info.getDataSource().getConnection();

            CallableStatement callableSt = conn.prepareCall("{call usp_content_search_select(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callableSt.setString(1, subject);
            callableSt.setString(2, body);
            callableSt.setString(3, fileName);
            callableSt.setString(4, categoryName);
            callableSt.setString(5, categoryId);
            callableSt.setString(6, tagName);
            callableSt.setBoolean(7, true);
            callableSt.setInt(8, multipleResultSet.getOffset());
            callableSt.setInt(9, multipleResultSet.getPageSize());
            callableSt.setString(10, orderStr);
            callableSt.registerOutParameter(11, Types.INTEGER);

            multipleResultSet.processResultSet(callableSt);

            multipleResultSet.setTotalCount(callableSt.getInt(11));

            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public MultipleResultSet findByAdminWithProc(String subject, String body, String fileName, String categoryName, String categoryId, String tagName, int page, int pageSize,
                                                 String orderStr) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        MultipleResultSet multipleResultSet = new MultipleResultSet(page, pageSize);
        try {
            conn = info.getDataSource().getConnection();

            CallableStatement callableSt = conn.prepareCall("{call usp_content_search_select(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callableSt.setString(1, subject);
            callableSt.setString(2, body);
            callableSt.setString(3, fileName);
            callableSt.setString(4, categoryName);
            callableSt.setString(5, categoryId);
            callableSt.setString(6, tagName);
            callableSt.setNull(7, Types.BOOLEAN);
            callableSt.setInt(8, multipleResultSet.getOffset());
            callableSt.setInt(9, multipleResultSet.getPageSize());
            callableSt.setString(10, orderStr);
            callableSt.registerOutParameter(11, Types.INTEGER);

            multipleResultSet.processResultSet(callableSt);

            multipleResultSet.setTotalCount(callableSt.getInt(11));

            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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


    public MultipleResultSet findWithMultipleProc(String searchType, String searchText, int page, int pageSize, String orderStr) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        MultipleResultSet multipleResultSet = new MultipleResultSet(page, pageSize);
        try {
            conn = info.getDataSource().getConnection();

            CallableStatement callableSt = conn.prepareCall("{call usp_content_search_multiple_select(?, ?, ?, ?, ?, ?, ?)}");
            callableSt.setString(1, searchType);
            callableSt.setString(2, searchText);
            callableSt.setBoolean(3, true);
            callableSt.setInt(4, multipleResultSet.getOffset());
            callableSt.setInt(5, multipleResultSet.getPageSize());
            callableSt.setString(6, orderStr);
            callableSt.registerOutParameter(7, Types.INTEGER);

            multipleResultSet.processResultSet(callableSt);

            multipleResultSet.setTotalCount(callableSt.getInt(7));

            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public MultipleResultSet findByAdminWithMultipleProc(String searchType, String searchText, int page, int pageSize, String orderStr) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        MultipleResultSet multipleResultSet = new MultipleResultSet(page, pageSize);
        try {
            conn = info.getDataSource().getConnection();

            CallableStatement callableSt = conn.prepareCall("{call usp_content_search_multiple_select(?, ?, ?, ?, ?, ?, ?)}");
            callableSt.setString(1, searchType);
            callableSt.setString(2, searchText);
            callableSt.setNull(3, Types.BOOLEAN);
            callableSt.setInt(4, multipleResultSet.getOffset());
            callableSt.setInt(5, multipleResultSet.getPageSize());
            callableSt.setString(6, orderStr);
            callableSt.registerOutParameter(7, Types.INTEGER);

            multipleResultSet.processResultSet(callableSt);

            multipleResultSet.setTotalCount(callableSt.getInt(7));

            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public void setMain(int id) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_content_set_main(?)}");
            callableSt.setInt(1, id);
            callableSt.execute();
            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (!conn.isClosed())
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteTempContent() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        Connection conn = null;

        try {
            conn = info.getDataSource().getConnection();
            CallableStatement callableSt = conn.prepareCall("{call usp_temp_content_delete()}");
            callableSt.execute();
            callableSt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (!conn.isClosed())
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Page<Integer> findIdsByConditions(boolean isAdmin, String searchType, String searchText, String categoryId, int page, int pageSize, String orderStr) {
        // tag =
        //########### 아래가 준비된 것
        // admin =
        // isAnd = else or
        // searchText = &(and), |(or)
        // searchText array
        // searchType = TITLE, CONTENT, FULL
        // categoryId =

        boolean isAnd = searchText.contains("&");
        List<String> tmpConditions = isAnd ? Arrays.asList(searchText.split("&")) : Arrays.asList(searchText.split("\\|"));
        List<String> conditions = tmpConditions.stream().map(String::trim).collect(Collectors.toList());

        List<Integer> list = contentMapper.findIdsByConditions(isAdmin, isAnd, searchType, conditions, page, pageSize, orderStr);
        int count = countMapper.getTotalCount();

        Page<Integer> pager = new Page<>();
        pager.setList(list);
        pager.setPage(page);
        pager.setPageSize(pageSize);
        pager.setTotalCount(count);

        return pager;

    }

    public List<Content> findContentsByIdInOrderByCreateDateDesc(List<Integer> ids) {
        return contentRepository.findContentsByIdInOrderByCreateDateDesc(ids);
    }


}

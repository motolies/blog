package kr.hvy.blog.service;

import kr.hvy.blog.entity.Category;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.entity.User;
import kr.hvy.blog.mapper.ContentMapper;
import kr.hvy.blog.mapper.CountMapper;
import kr.hvy.blog.model.ContentPrevNext;
import kr.hvy.blog.model.base.Page;
import kr.hvy.blog.model.request.SearchObjectDto;
import kr.hvy.blog.model.response.ContentNoBodyDto;
import kr.hvy.blog.repository.ContentRepository;
import kr.hvy.blog.repository.TagRepository;
import kr.hvy.blog.repository.UserRepository;
import kr.hvy.blog.util.AuthorizationUtil;
import kr.hvy.blog.util.FileUtil;
import kr.hvy.blog.util.MultipleResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContentService {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;

    private final ContentRepository contentRepository;

    private final CategoryService categoryService;

    private final CountMapper countMapper;

    private final ContentMapper contentMapper;

    private final TagRepository tagRepository;

    private static Path rootLocation;

    @Value("${path.upload}")
    public void setRootLocation(String path) {
        this.rootLocation = Paths.get(path);
    }

    public List<Content> findAll() {
        return contentRepository.findAll();
    }

    public Content newContent() {
        User user = userRepository.findById(AuthorizationUtil.getUserId()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        this.deleteTempContent();
        Category cat = categoryService.findById("ROOT");
        Content content = new Content();
        content.setUser(user);
        content.setCategory(cat);
        contentRepository.saveAndFlush(content);
        int contentId = content.getId();
        em.detach(content);
        return contentRepository.findById(contentId).orElseThrow(() -> new IllegalArgumentException("컨텐츠가 존재하지 않습니다."));
    }

    public Content findById(int id) {
        Content content = contentRepository.findById(id).orElse(null);

        if (content == null || (!content.isPublic() && !AuthorizationUtil.hasAdminRole())) {
            content = null;
        } else {
            content.setViewCount(content.getViewCount() + 1);
            setPrevNext(content);
            this.save(content);
        }
        return content;
    }

    @Transactional
    public Content save(Content content) {
        return contentRepository.save(content);
    }

    @Transactional
    public Content update(Content content, Content newContent) {
        content.setSubject(newContent.getSubject());
        content.setBody(newContent.getBody());

        Category cat = categoryService.findById(newContent.getCategoryId());
        content.setCategory(cat);

        content.setCategoryId(newContent.getCategoryId());
        content.setPublic(newContent.isPublic());

        // 태그도 바로바로 달도록 하면 별도로 여기서 관리하지 않아도 되겠다.
//        // 기존꺼 삭제
//        Set<Tag> oldTags = tagRepository.findByIdIn(content.getTag().stream().map(t -> t.getId()).collect(Collectors.toSet()));
//        for (Tag tag : oldTags) {
//            content.removeTag(tag);
//        }
//
//        // 신규 추가
//        Set<Tag> newTags = new HashSet<>();
//        if (newContent.getTag().size() > 0) {
//            newTags = tagRepository.findByIdIn(newContent.getTag().stream().map(t -> t.getId()).collect(Collectors.toSet()));
//        }
//        for (Tag tag : newTags) {
//            content.addTag(tag);
//        }

        contentRepository.saveAndFlush(content);
        int contentId = content.getId();
        em.detach(content);
        return contentRepository.findById(contentId).orElse(null);
    }

    private void setPrevNext(Content content) {
        ContentPrevNext prevNext = contentMapper.findPrevNextById(AuthorizationUtil.hasAdminRole(), content.getId());
        content.setPrev(prevNext.getPrev());
        content.setNext(prevNext.getNext());
    }

    public ContentPrevNext findPrevNextById(int id) {
        return contentMapper.findPrevNextById(AuthorizationUtil.hasAdminRole(), id);
    }

    public Content findByMain() {
        Content content = contentRepository.findByIsMainTrue();

        if (content == null || (!content.isPublic() && !AuthorizationUtil.hasAdminRole())) {
            content = new Content();
        } else {
            content.setViewCount(content.getViewCount() + 1);
            setPrevNext(content);
            this.save(content);
        }
        return content;
    }

    @Transactional
    public void deleteById(int id) {
        Content content = contentRepository.findById(id).orElse(null);
        if (content != null && content.getFile().size() > 0) {
            FileUtil.deleteFolder(rootLocation.toString(), id);
        }
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
        List<Integer> tempList = contentMapper.findByTempContent();
        for (Integer id : tempList) {
            deleteById(id);
        }
    }

    public Page<ContentNoBodyDto> findBySearchObject(boolean isAdmin, SearchObjectDto searchObjectDto) {
        List<ContentNoBodyDto> list = contentMapper.findBySearchObject(isAdmin, searchObjectDto);
        int count = countMapper.getTotalCount();

        Page<ContentNoBodyDto> pager = new Page<>();
        pager.setList(list);
        pager.setPage(searchObjectDto.getPage());
        pager.setPageSize(searchObjectDto.getPageSize());
        pager.setTotalCount(count);
        return pager;
    }


}

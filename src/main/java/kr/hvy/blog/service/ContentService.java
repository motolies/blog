package kr.hvy.blog.service;

import kr.hvy.blog.entity.Content;
import kr.hvy.blog.util.MultipleResultSet;
import org.springframework.security.core.Authentication;

public interface ContentService {
    Content findById(int id);

    Content findBySyncKey(String synckey);

    void save(Content content);

    Content findByMain(Authentication authentication);

    void deleteById(int id);

    MultipleResultSet findWithProc(String subject, String body, String fileName, String categoryName, String categoryId, String tagName, int page, int pageSize, String orderStr);

    MultipleResultSet findByAdminWithProc(String subject, String body, String fileName, String categoryName, String categoryId, String tagName, int page, int pageSize, String orderStr);

    MultipleResultSet findWithMultipleProc(String searchType, String searchText, int page, int pageSize, String orderStr);

    MultipleResultSet findByAdminWithMultipleProc(String searchType, String searchText, int page, int pageSize, String orderStr);

    void setMain(int id);

    void deleteTempContent();
}
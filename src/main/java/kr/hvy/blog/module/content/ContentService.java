package kr.hvy.blog.module.content;

import io.github.motolies.util.time.TimeUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import kr.hvy.blog.infra.core.Page;
import kr.hvy.blog.module.auth.AuthorizationUtil;
import kr.hvy.blog.module.auth.UserRepository;
import kr.hvy.blog.module.auth.domain.User;
import kr.hvy.blog.module.category.CategoryService;
import kr.hvy.blog.module.category.domain.Category;
import kr.hvy.blog.module.content.domain.Content;
import kr.hvy.blog.module.content.dto.ContentNoBodyResponse;
import kr.hvy.blog.module.content.dto.ContentPrevNextResponse;
import kr.hvy.blog.module.content.dto.SearchObjectDto;
import kr.hvy.blog.module.content.mapper.ContentMapper;
import kr.hvy.blog.module.content.mapper.CountMapper;
import kr.hvy.blog.module.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private static Path rootLocation;

  @Value("${path.upload}")
  public void setRootLocation(String path) {
    rootLocation = Paths.get(path);
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
      save(content);
    }
    return content;
  }

  @Transactional
  public Content save(Content content) {
    return contentRepository.save(content);
  }

  @Transactional
  public Content update(Content content, Content newContent) {
    // 본문 또는 제목이 변경되었을 경우만 updateDate를 변경한다.
    if (!(content.getSubject().equals(newContent.getSubject()) && content.getBody().equals(newContent.getBody()))) {
      content.setUpdateDate(TimeUtil.getUtcTimestamp());
    }

    content.setSubject(newContent.getSubject());
    content.setBody(newContent.getBody());

    Category cat = categoryService.findById(newContent.getCategoryId());
    content.setCategory(cat);

    content.setCategoryId(newContent.getCategoryId());
    content.setPublic(newContent.isPublic());

    contentRepository.saveAndFlush(content);
    int contentId = content.getId();
    em.detach(content);
    return contentRepository.findById(contentId).orElseThrow(() -> new IllegalArgumentException("컨텐츠가 존재하지 않습니다."));
  }

  private void setPrevNext(Content content) {
    ContentPrevNextResponse prevNext = contentMapper.findPrevNextById(AuthorizationUtil.hasAdminRole(), content.getId());
    content.setPrev(prevNext.getPrev());
    content.setNext(prevNext.getNext());
  }

  public ContentPrevNextResponse findPrevNextById(int id) {
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
    Content content = contentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("컨텐츠가 존재하지 않습니다."));
    if (CollectionUtils.isNotEmpty(content.getFile())) {
      FileUtil.deleteFolder(rootLocation.toString(), id);
    }
    contentRepository.deleteById(id);
  }

  public void setMain(int id) {
    contentMapper.setMain(id);
  }

  public void deleteTempContent() {
    List<Integer> tempList = contentMapper.findByTempContent();
    for (Integer id : tempList) {
      deleteById(id);
    }
  }

  public Page<ContentNoBodyResponse> findBySearchObject(boolean isAdmin, SearchObjectDto searchObjectDto) {
    List<ContentNoBodyResponse> list = contentMapper.findBySearchObject(isAdmin, searchObjectDto);
    int count = countMapper.getTotalCount();

    Page<ContentNoBodyResponse> pager = new Page<>();
    pager.setList(list);
    pager.setPage(searchObjectDto.getPage());
    pager.setPageSize(searchObjectDto.getPageSize());
    pager.setTotalCount(count);
    return pager;
  }

  public List<Integer> findByPublicContent() {
    return contentMapper.findByPublicContent();
  }


}

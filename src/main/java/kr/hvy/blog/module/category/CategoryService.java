package kr.hvy.blog.module.category;

import java.sql.SQLException;
import java.util.List;
import kr.hvy.blog.module.category.domain.Category;
import kr.hvy.blog.module.category.dto.CategorySaveDto;
import kr.hvy.blog.module.category.dto.CategoryFlatResponseDto;
import kr.hvy.blog.module.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryMapper categoryMapper;
  private final CategoryRepository categoryRepository;


  public List<CategoryFlatResponseDto> findAllCategory() {
    return categoryMapper.findAllCategory();
  }

  public Category findById(String id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
  }

  public Category findRoot() {
    return categoryRepository.findById("ROOT")
        .orElseThrow(() -> new IllegalArgumentException("루트 카테고리가 존재하지 않습니다."));
  }


  @Deprecated
  @Transactional
  public List<Category> saveAll(List<Category> entities) {
    List<Category> list = categoryRepository.saveAll(entities);
    categoryRepository.flush();
    return list;
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

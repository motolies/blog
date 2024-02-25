package kr.hvy.blog.mapper;

import kr.hvy.blog.model.request.CategorySaveDto;
import kr.hvy.blog.model.response.CategoryFlatResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryFlatResponseDto> findAllCategory();

    void saveCategory(@Param("categorySaveDto") CategorySaveDto categorySaveDto);

    void updateFullName();

    void deleteById(@Param("categoryId") String categoryId);
}

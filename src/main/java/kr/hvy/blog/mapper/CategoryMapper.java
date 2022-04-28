package kr.hvy.blog.mapper;

import kr.hvy.blog.model.response.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> findAllCategory();

}

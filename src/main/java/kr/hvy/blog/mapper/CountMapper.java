package kr.hvy.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CountMapper {

    @Select("SELECT FOUND_ROWS()")
    int getTotalCount();

}

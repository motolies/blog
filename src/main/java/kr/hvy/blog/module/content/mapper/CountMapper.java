package kr.hvy.blog.module.content.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CountMapper {


    @Options(useCache = false, flushCache = Options.FlushCachePolicy.TRUE)
    @Select("SELECT FOUND_ROWS()")
    int getTotalCount();

}

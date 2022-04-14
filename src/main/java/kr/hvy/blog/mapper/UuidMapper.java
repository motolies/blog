package kr.hvy.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UuidMapper {


    @Options(useCache = false, flushCache = Options.FlushCachePolicy.TRUE)
    @Select("SELECT HEX(FN_ORDERED_UUID())")
    String getUuid();


    String uuid();


}

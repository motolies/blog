package kr.hvy.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UuidMapper {

//    @Select("SELECT HEX(FN_ORDERED_UUID())")
    String uuid();

}

package kr.hvy.blog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentMapper {

    List<Integer> findIdsByConditions(boolean isAdmin, boolean isAnd, String searchType, List<String> conditions, int page, int pageSize, String orderStr);

}

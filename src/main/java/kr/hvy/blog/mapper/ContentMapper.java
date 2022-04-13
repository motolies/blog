package kr.hvy.blog.mapper;

import kr.hvy.blog.model.response.ContentNoBody;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentMapper {

    List<ContentNoBody> findIdsByConditions(boolean isAdmin, boolean isAnd, String searchType, List<String> conditions, int page, int pageSize);

}

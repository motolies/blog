package kr.hvy.blog.mapper;

import kr.hvy.blog.model.response.ContentNoBodyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentMapper {

    List<ContentNoBodyDto> findIdsByConditions(boolean isAdmin, boolean isAnd, String searchType, List<String> conditions, String categoryId, int page, int pageSize);

}

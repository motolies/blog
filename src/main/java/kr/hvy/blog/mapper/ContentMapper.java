package kr.hvy.blog.mapper;

import kr.hvy.blog.model.ContentPrevNext;
import kr.hvy.blog.model.request.SearchObjectDto;
import kr.hvy.blog.model.response.ContentNoBodyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContentMapper {

    List<ContentNoBodyDto> findBySearchObject(boolean isAdmin, @Param("obj") SearchObjectDto searchObjectDto);

    ContentPrevNext findPrevNextById(boolean isAdmin, int id);

    List<Integer> findByTempContent();

}

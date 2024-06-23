package kr.hvy.blog.module.content.mapper;

import kr.hvy.blog.module.content.dto.ContentPrevNextResponse;
import kr.hvy.blog.module.content.dto.SearchObjectDto;
import kr.hvy.blog.module.content.dto.ContentNoBodyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContentMapper {

    List<ContentNoBodyResponse> findBySearchObject(boolean isAdmin, @Param("obj") SearchObjectDto searchObjectDto);

    ContentPrevNextResponse findPrevNextById(boolean isAdmin, int id);

    List<Integer> findByTempContent();

    List<Integer> findByPublicContent();

    void setMain(@Param("id") int id);
}

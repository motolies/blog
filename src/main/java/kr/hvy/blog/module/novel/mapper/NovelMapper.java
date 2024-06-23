package kr.hvy.blog.module.novel.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NovelMapper {
    List<Integer> findSeqByTitle(String title);
}

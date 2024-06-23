package kr.hvy.blog.module.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovelDownRequest {
    private String listUrl;
    private String title;
}

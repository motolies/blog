package kr.hvy.blog.model.novel;

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

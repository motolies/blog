package kr.hvy.blog.module.content.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentDeleteResponse {
    private String id;
}

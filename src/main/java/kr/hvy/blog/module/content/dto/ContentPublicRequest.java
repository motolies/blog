package kr.hvy.blog.module.content.dto;

import lombok.Data;

@Data
public class ContentPublicRequest {
    private int id;
    private boolean isPublicStatus;
}

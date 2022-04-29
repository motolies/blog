package kr.hvy.blog.model.request;

import lombok.Data;

@Data
public class ContentPublicDto {
    private int id;
    private boolean isPublicStatus;
}

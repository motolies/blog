package kr.hvy.blog.module.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDeleteResponse {
    private String id;
}

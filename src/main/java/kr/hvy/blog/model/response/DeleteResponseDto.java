package kr.hvy.blog.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteResponseDto {
    private String id;
}

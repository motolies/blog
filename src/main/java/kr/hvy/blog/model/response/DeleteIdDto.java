package kr.hvy.blog.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteIdDto {
    private String id;
}

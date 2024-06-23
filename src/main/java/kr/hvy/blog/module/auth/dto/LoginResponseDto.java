package kr.hvy.blog.module.auth.dto;

import java.io.Serial;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1421099225645521465L;

    private String token;
}

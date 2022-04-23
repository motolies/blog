package kr.hvy.blog.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginResponseDto implements Serializable {

    private static final long serialVersionUID = -1421099225645521465L;

    private String token;
}

package kr.hvy.blog.module.auth.dto;

import kr.hvy.blog.module.auth.domain.AuthorityName;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class MyProfileResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8392979470355268638L;

    private String LoginId;
    private String UserName;
    List<AuthorityName> Role;
}

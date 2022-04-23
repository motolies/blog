package kr.hvy.blog.model.response;

import kr.hvy.blog.entity.AuthorityName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyProfileDto {
    private String LoginId;
    private String UserName;
    List<AuthorityName> Role;
}

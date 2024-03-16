package kr.hvy.blog.entity;

import io.github.motolies.code.CommonEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorityName implements CommonEnumCode<String> {
    ROLE_USER("ROLE_USER", "사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String code;
    private final String desc;
}

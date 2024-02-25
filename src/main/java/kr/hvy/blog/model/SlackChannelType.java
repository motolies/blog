package kr.hvy.blog.model;

import io.github.motolies.code.CommonEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackChannelType implements CommonEnumCode<String> {
    HVY_NOTIFY("NOTIFY", "알림채널", "#hvy-notify"),
    HVY_ERROR("ERROR", "에러채널", "#hvy-error");

    private final String code;
    private final String desc;
    private final String channel;

}

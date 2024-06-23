package kr.hvy.blog.module.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchConditionDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6372195696197437659L;

    @Schema(name = "logic", example = "AND | OR")
    private String logic;

    @Schema(name = "keywords", example = "[{\"name\": \"검색어\"}]")
    private List<SearchElementDto> keywords;

    @Override
    public String toString() {
        return "SearchConditionDto{" +
                "logic='" + logic + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}

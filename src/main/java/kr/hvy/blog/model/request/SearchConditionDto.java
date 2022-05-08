package kr.hvy.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SearchConditionDto implements Serializable {

    private static final long serialVersionUID = 6372195696197437659L;

    @Schema(name = "logic", example = "AND | OR")
    private String logic;

    @Schema(name = "keywords", example = "[\"검색\", \"키워드\"]")
    private List<String> keywords;

    @Override
    public String toString() {
        return "SearchConditionDto{" +
                "logic='" + logic + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}

package kr.hvy.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SearchObjectDto implements Serializable {

    private static final long serialVersionUID = 4289845041651026684L;

    @Schema(name = "searchType", example = "TITLE | CONTENT | FULL")
    private String searchType;

    private SearchConditionDto searchCondition;

    private List<String> categoryIds;

    private List<String> tagIds;

    private int page;

    private int pageSize;

    @Override
    public String toString() {
        return "SearchObjectDto{" +
                "searchType='" + searchType + '\'' +
                ", searchCondition=" + searchCondition +
                ", categoryIds=" + categoryIds +
                ", tagIds=" + tagIds +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}

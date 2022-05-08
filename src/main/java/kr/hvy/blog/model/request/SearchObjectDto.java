package kr.hvy.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchObjectDto implements Serializable {

    private static final long serialVersionUID = 4289845041651026684L;

    @Schema(name = "searchType", example = "TITLE | CONTENT | FULL")
    private String searchType;

    private SearchConditionDto searchCondition;

    @Schema(name = "categories", example = "[{\"id\": \"ROOT\", \"name\": \"전체글\"}]")
    private List<SearchElementDto> categories;

    @Schema(name = "categories", example = "[{\"id\": \"1\", \"name\": \"Java\"}]")
    private List<SearchElementDto> tags;

    private int page;

    private int pageSize;

    @Override
    public String toString() {
        return "SearchObjectDto{" +
                "searchType='" + searchType + '\'' +
                ", searchCondition=" + searchCondition +
                ", categories=" + categories +
                ", tags=" + tags +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}

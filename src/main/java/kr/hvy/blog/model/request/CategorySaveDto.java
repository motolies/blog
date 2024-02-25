package kr.hvy.blog.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.hvy.blog.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값은 반환하지 않음
public class CategorySaveDto {
    private String id;
    @JsonProperty("pId")
    private String pId;
    private String name;
    private String fullPath;
    private String fullName;
    private int order = 0;
    private int contentCount;
    private List<CategorySaveDto> children;

    // Category 엔티티를 기반으로 DTO를 생성하는 메서드
    public static CategorySaveDto fromEntity(Category category) {
        CategorySaveDto dto = new CategorySaveDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setFullPath(category.getFullPath());
        dto.setContentCount(category.getContentCount());

        // 하위 카테고리도 DTO로 변환하여 설정
        if (category.getCategory() != null && !category.getCategory().isEmpty()) {
            List<CategorySaveDto> children = category.getCategory().stream()
                    .map(CategorySaveDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setChildren(children);
        }

        return dto;
    }

    public String getFullPath() {
        return "/" + this.name + "/";
    }

    public String getFullName() {
        return "/" + this.name + "/";
    }

}

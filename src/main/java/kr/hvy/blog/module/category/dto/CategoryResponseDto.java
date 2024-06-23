package kr.hvy.blog.module.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.hvy.blog.module.category.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값은 반환하지 않음
public class CategoryResponseDto {
    private String id;
    @JsonProperty("pId")
    private String pId;
    private String name;
    private String fullPath;
    private int contentCount;
    private List<CategoryResponseDto> children;

    // Category 엔티티를 기반으로 DTO를 생성하는 메서드
    public static CategoryResponseDto fromEntity(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setPId(category.getPId());
        dto.setName(category.getName());
        dto.setFullPath(category.getFullPath());
        dto.setContentCount(category.getContentCount());

        // 하위 카테고리도 DTO로 변환하여 설정
        if (category.getCategory() != null && !category.getCategory().isEmpty()) {
            List<CategoryResponseDto> children = category.getCategory().stream()
                    .map(CategoryResponseDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setChildren(children);
        }

        return dto;
    }
}

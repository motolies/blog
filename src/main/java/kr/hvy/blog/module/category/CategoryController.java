package kr.hvy.blog.module.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.module.category.domain.Category;
import kr.hvy.blog.module.category.dto.CategorySaveDto;
import kr.hvy.blog.module.category.dto.CategoryResponseDto;
import kr.hvy.blog.module.category.dto.CategoryFlatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Category")
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "전체 카테고리 조회(포스트에서 카테고리 표기시에 쓰임)")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Category.class))})
    @GetMapping("")
    public ResponseEntity<?> getCategory() {
        List<CategoryFlatResponseDto> list = categoryService.findAllCategory();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "루트 카테고리 조회")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Category.class))})
    @GetMapping("/root")
    public ResponseEntity<?> getRootCategory() {

        Category category = categoryService.findRoot();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CategoryResponseDto.fromEntity(category));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "카테고리 저장", description = "id, name, parent 가 최소 정보이다.")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Category.class))})
    @PostMapping("")
    public ResponseEntity<?> saveCategory(@RequestBody CategorySaveDto categorySaveDto) throws SQLException {
        categoryService.saveWithProc(categorySaveDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categorySaveDto);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "카테고리 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) throws SQLException {
        categoryService.deleteWithProc(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

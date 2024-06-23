package kr.hvy.blog.module.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hvy.blog.module.tag.domain.Tag;
import kr.hvy.blog.module.tag.dto.TagDeleteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "포스트 태그 관리")
@RequestMapping("/api/tag")
public class TagController {

  private final TagService tagService;

  @Operation(summary = "모든 태그 조회")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Tag.class))})
  @GetMapping("/all")
  public ResponseEntity<?> getAllTags() {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findAll());
  }

  @Operation(summary = "태그 검색")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Tag.class))})
  @GetMapping("")
  public ResponseEntity<?> getTags(@RequestParam(defaultValue = "") String name) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.findByNameContainingOrderByName(name));
  }

  @Operation(summary = "태그 저장")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Tag.class))})
  @PostMapping("")
  public ResponseEntity<?> saveTag(@RequestBody Tag tag) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.save(tag));
  }

  @Operation(summary = "태그 업데이트")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Tag.class))})
  @PutMapping("/{tagId}")
  public ResponseEntity<?> updateTag(@PathVariable int tagId, @RequestBody Tag tag) {

    throw new NotImplementedException("Not Implemented");

//        return repository.findById(id)
//          .map(employee -> {
//            employee.setName(newEmployee.getName());
//            employee.setRole(newEmployee.getRole());
//            return repository.save(employee);
//          })
//          .orElseGet(() -> {
//            newEmployee.setId(id);
//            return repository.save(newEmployee);
//          });
  }

  @Operation(summary = "태그 삭제")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Tag.class))})
  @DeleteMapping("/{tagId}")
  public ResponseEntity<?> deleteTag(@PathVariable int tagId) {
    tagService.deleteById(tagId);
    return ResponseEntity.status(HttpStatus.OK).body(TagDeleteResponse.builder().id(String.valueOf(tagId)).build());
  }

}

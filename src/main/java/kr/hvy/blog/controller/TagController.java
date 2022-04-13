package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hvy.blog.entity.Tag;
import kr.hvy.blog.model.response.DeleteResponseDto;
import kr.hvy.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "포스트 태그 관리")
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 검색")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Tag.class))})
    @GetMapping("")
    public ResponseEntity getTags(@RequestParam(defaultValue = "") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.findByNameContainingOrderByName(name));
    }

    @Operation(summary = "태그 저장")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Tag.class))})
    @PostMapping("")
    public ResponseEntity saveTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.save(tag));
    }

    @Operation(summary = "태그 업데이트")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Tag.class))})
    @PutMapping("/{tagId}")
    public ResponseEntity updateTag(@PathVariable int tagId, @RequestBody Tag tag) {

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
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Tag.class))})
    @DeleteMapping("/{tagId}")
    public ResponseEntity deleteTag(@PathVariable int tagId) {
        tagService.deleteById(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(DeleteResponseDto.builder().id(String.valueOf(tagId)).build());
    }

}

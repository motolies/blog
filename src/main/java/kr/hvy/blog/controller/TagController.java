package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Tag", description = "포스트 태그 관리")
@RequestMapping("/api/tag")
public class TagController {

    @Operation(summary = "태그 검색")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Tag.class))})
    @GetMapping("")
    public ResponseEntity getTags(@RequestParam(defaultValue = "") String name) {
        throw new NotImplementedException("Not Implemented");
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
        throw new NotImplementedException("Not Implemented");
    }

}

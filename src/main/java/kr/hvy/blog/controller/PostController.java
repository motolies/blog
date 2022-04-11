package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.model.base.Page;
import kr.hvy.blog.service.ContentService;
import kr.hvy.blog.util.AuthorizationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final ContentService contentService;

    @Operation(summary = "메인화면으로 표기할 포스트를 조회한다")
    @ApiResponse(responseCode = "200", content = {
            @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))}
    )
    @GetMapping(value = {""})
    public ResponseEntity getMain(Authentication auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contentService.findByMain(auth));
    }

    @Operation(summary = "검색")
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/search"})
    public ResponseEntity getMultipleList2(
            Authentication auth,
            @RequestParam(defaultValue = "TITLE") String searchType,
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "") String categoryId, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int pageSize, @RequestParam(defaultValue = "CreateDate DESC") String orderStr) {

        Page<Integer> ids = contentService.findIdsByConditions(AuthorizationProvider.hasAdminRole(), searchType, searchText, categoryId, page, pageSize, orderStr);

        // 이게 실제 데이터를 주는 부분이다.. 요거를 contentService.findIdsByConditions 에서 리턴하도록 바꿔야 한다.
        return ResponseEntity.status(HttpStatus.OK).body(
                contentService.findContentsByIdInOrderByCreateDateDesc(ids.getList())
        );
    }

}

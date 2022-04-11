package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.model.Content;
import kr.hvy.blog.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/")
public class PostController {

    private final ContentService contentService;


    @Operation(summary = "메인화면으로 표기할 포스트를 조회한다")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.model.Content.class))})
    @GetMapping(value = {""})
    public ResponseEntity getMain(Authentication auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contentService.findByMain(auth));
    }

}

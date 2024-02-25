package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.service.SearchEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "SearchEngine")
@RequestMapping("/api/search")
public class SearchEngineController {

    private final SearchEngineService searchEngineService;

    @Operation(summary = "메인화면에서 표기할 검색엔진을 표기한다")
    @ApiResponse(responseCode = "200", content = {
            @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Content.class))}
    )
    @GetMapping(value = {""})
    public ResponseEntity<?> getEngines() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(searchEngineService.findAll());
    }

}

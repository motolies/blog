package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.service.UuidService;
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
@Tag(name = "Test", description = "테스트용")
@RequestMapping("/api/test")
public class _TestController {

    private final UuidService uuidService;

    @Operation(summary = "UUID")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = {"uuid"})
    public ResponseEntity UUID() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uuidService.uuid());
    }

}

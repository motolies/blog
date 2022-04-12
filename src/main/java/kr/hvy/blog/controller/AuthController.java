package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.model.request.LoginDto;
import kr.hvy.blog.service.UserService;
import kr.hvy.blog.util.CookieProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "로그인, 로그아웃")
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.header.name}")
    private String tokenHeader;

    private final UserService userService;

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping(value = {"login"})
    public ResponseEntity login(@RequestBody LoginDto loginDto) {

        String token = userService.login(loginDto);
        ResponseCookie springCookie = CookieProvider.setSpringCookie(tokenHeader, token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = {"logout"})
    public ResponseEntity logout() {
        ResponseCookie springCookie = CookieProvider.removeSpringCookie(tokenHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();

    }
}

package kr.hvy.blog.controller;

import kr.hvy.blog.service.NovelDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/novel")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class NovelDownloadController {

    private final NovelDownloadService novelDownloadService;

    @GetMapping("/test")
    public ResponseEntity test() throws InterruptedException {
        novelDownloadService.download();
        return ResponseEntity.ok("ok");
    }


}

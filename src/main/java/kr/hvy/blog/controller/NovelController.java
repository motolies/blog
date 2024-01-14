package kr.hvy.blog.controller;

import kr.hvy.blog.model.novel.NovelDownRequest;
import kr.hvy.blog.service.NovelService;
import kr.hvy.blog.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/novel")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;
    private final SlackService slackService;

    @PostMapping("/down")
    public ResponseEntity downloadNovel(@RequestBody NovelDownRequest request) throws InterruptedException {
        novelService.download(request);
        slackService.send("다운로드 접수함");
        return ResponseEntity.ok("ok");
    }


}

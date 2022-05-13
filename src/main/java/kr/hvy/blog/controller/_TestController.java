package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.service.ContentService;
import kr.hvy.blog.service.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Test", description = "테스트용")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/test")
public class _TestController {

    private final UuidService uuidService;
    private final ContentService contentService;

    @Operation(summary = "/file?id => /api/file/id 로 변경")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = {"file-api-change"})
    public ResponseEntity fileApiChange() {
        List<Content> list = contentService.findAll();

        for (Content content : list) {
            String newBody = regexUrlReplace(content.getBody());
            content.setBody(newBody);
            contentService.save(content);
        }

        return ResponseEntity.ok("ok");
    }

    private String regexUrlReplace(String html) {
        // https://stackoverflow.com/questions/43371521/replaceall-with-java8-lambda-functions
        return Pattern.compile("(href|src)[\\s+]?=[\\s+]?[\"|']\\/file\\?id=(.*?)[\"|']").matcher(html).replaceAll(m -> changeUrlAPI(m));
    }

    private String changeUrlAPI(MatchResult matchResult) {
        return matchResult.group(1) + "='/api/file/" + matchResult.group(2) + "'";
    }

}

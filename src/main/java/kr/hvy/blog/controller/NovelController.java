package kr.hvy.blog.controller;

import kr.hvy.blog.model.novel.NovelDownRequest;
import kr.hvy.blog.service.NovelService;
import kr.hvy.blog.util.SlackMessenger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

@Slf4j
@RestController
@RequestMapping("/api/novel")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;

    @PostMapping("/down")
    public ResponseEntity downloadNovel(@RequestBody NovelDownRequest request) throws InterruptedException, IOException {
        novelService.download(request);
        SlackMessenger.send(String.format("%s 다운로드 접수함", request.getTitle()));
        return ResponseEntity.ok("ok");
    }

    @GetMapping(value = "/down/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadNovel(@PathVariable String title) throws UnsupportedEncodingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = MessageFormat.format("{0}.txt", title);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream novelStream = novelService.getTxtFile(title)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = novelStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                log.error("txt 다운로드 에러", e);
            }
        };

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

}

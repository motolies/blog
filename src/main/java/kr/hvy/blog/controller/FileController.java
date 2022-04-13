package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.entity.File;
import kr.hvy.blog.model.request.FileDto;
import kr.hvy.blog.model.response.DeleteIdDto;
import kr.hvy.blog.service.FileService;
import kr.hvy.blog.util.AuthorizationProvider;
import kr.hvy.blog.util.ByteHelper;
import kr.hvy.blog.util.MediaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "File")
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 아이디로 조회 후 파일을 다운로드 한다.")
    @GetMapping(value = {"/{fileId}"})
    public ResponseEntity serveFile(@PathVariable String fileId) {
        try {
            byte[] fId = ByteHelper.hexToByteArray(fileId);
            File file = fileService.load(fId);

            Content content = file.getContent();

            if (content == null || (!content.isPublic() && !AuthorizationProvider.hasAdminRole())) {
                // 권한없음으로 받지 못함
                return ResponseEntity.notFound().build();
            } else {
                // 권한이 있으니까 받음
                HttpHeaders headers = new HttpHeaders();

                String fileName = file.getOriginFileName();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

                if (MediaUtils.containsImageMediaType(file.getType())) {
                    headers.setContentType(MediaType.valueOf(file.getType()));
                } else {
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }

                Resource resource = fileService.loadAsResource(file.getPath());
                return ResponseEntity.ok().headers(headers).body(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "파일 업로드 저장")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.File.class))})
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(@ModelAttribute FileDto fileDto) {
        try {
            File uploadedFile = fileService.store(fileDto.getFile(), fileDto.getContentId());
            return new ResponseEntity<>(uploadedFile, HttpStatus.OK);
        } catch (Exception e) {
            log.error("file upload errpr", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트로 파일 목록 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list/{contentId}")
    public ResponseEntity findFilesByContentId(@PathVariable int contentId) {
        List<File> file = fileService.findByContentId(contentId);
        file.sort((File f1, File f2) -> f1.getOriginFileName().compareTo(f2.getOriginFileName()));
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "파일 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{fileId}")
    public ResponseEntity delete(@PathVariable String fileId) {
        byte[] bId = ByteHelper.hexToByteArray(fileId);
        fileService.deleteById(bId);
        return new ResponseEntity<>(DeleteIdDto.builder().id(fileId).build(), HttpStatus.OK);
    }


}

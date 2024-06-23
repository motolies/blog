package kr.hvy.blog.module.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kr.hvy.blog.infra.support.ByteUtil;
import kr.hvy.blog.module.auth.AuthorizationUtil;
import kr.hvy.blog.module.content.domain.Content;
import kr.hvy.blog.module.file.domain.File;
import kr.hvy.blog.module.file.dto.FileDeleteResponse;
import kr.hvy.blog.module.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "File")
@RequestMapping("/api/file")
public class FileController {

  private final FileService fileService;

  @Operation(summary = "파일 아이디로 조회 후 파일을 다운로드 한다.")
  @GetMapping(value = {"/{fileId}"})
  public ResponseEntity<?> serveFile(@PathVariable String fileId) {
    try {
      byte[] fId = ByteUtil.hexToByteArray(fileId);
      File file = fileService.load(fId);

      Content content = file.getContent();

      if (content == null || (!content.isPublic() && !AuthorizationUtil.hasAdminRole())) {
        // 권한없음으로 받지 못함
        return ResponseEntity.notFound().build();
      } else {
        // 권한이 있으니까 받음
        HttpHeaders headers = new HttpHeaders();

        String fileName = file.getOriginFileName();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");

        if (MediaUtils.containsImageMediaType(file.getType())) {
          headers.setContentType(MediaType.valueOf(file.getType()));
        } else {
          headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        Resource resource = fileService.loadAsResource(file.getPath());
        return ResponseEntity.ok().headers(headers).body(resource);
      }
    } catch (Exception e) {
      log.error("file download error", e);
      return ResponseEntity.badRequest().build();
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "파일 업로드 저장")
  @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = File.class))})
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> upload(@ModelAttribute FileDto fileDto) {
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
  public ResponseEntity<?> findFilesByContentId(@PathVariable int contentId) {
    List<File> file = fileService.findByContentId(contentId);
    file.sort((File f1, File f2) -> f1.getOriginFileName().compareTo(f2.getOriginFileName()));
    return new ResponseEntity<>(file, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "파일 삭제")
  @ApiResponse(responseCode = "200")
  @DeleteMapping("/{fileId}")
  public ResponseEntity<?> delete(@PathVariable String fileId) {
    byte[] bId = ByteUtil.hexToByteArray(fileId);
    fileService.deleteById(bId);
    return ResponseEntity.status(HttpStatus.OK).body(FileDeleteResponse.builder().id(fileId).build());
  }


}

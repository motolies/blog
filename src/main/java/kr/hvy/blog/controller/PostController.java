package kr.hvy.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.entity.Tag;
import kr.hvy.blog.model.base.Page;
import kr.hvy.blog.model.request.ContentPublicDto;
import kr.hvy.blog.model.request.ContentTagDto;
import kr.hvy.blog.model.request.SearchObjectDto;
import kr.hvy.blog.model.response.ContentNoBodyDto;
import kr.hvy.blog.model.response.DeleteResponseDto;
import kr.hvy.blog.service.ContentService;
import kr.hvy.blog.service.TagService;
import kr.hvy.blog.util.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final ContentService contentService;

    private final TagService tagService;

    @Operation(summary = "메인화면으로 표기할 포스트를 조회한다")
    @ApiResponse(responseCode = "200", content = {
            @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))}
    )
    @GetMapping(value = {""})
    public ResponseEntity getMain() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contentService.findByMain());
    }

    @Operation(summary = "단일 포스트 조회")
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/{contentId}"})
    public ResponseEntity getContent(@PathVariable int contentId) {
        return ResponseEntity.status(HttpStatus.OK).body(contentService.findById(contentId));
    }

    @Operation(summary = "포스트 이전/이후 글 번호 조회")
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/prev-next/{contentId}"})
    public ResponseEntity getContentPrevNext(@PathVariable int contentId) {
        return ResponseEntity.status(HttpStatus.OK).body(contentService.findPrevNextById(contentId));
    }

    @Operation(summary = "검색상세")
    @Parameter(name = "query", description = "BASE64로 인코딩한 파라미터(json object)", required = true)
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/search"})
    public ResponseEntity searchDetail(
            Authentication auth,
            @RequestParam String query) throws JsonProcessingException {
        String decodedQuery = new String(Base64.getDecoder().decode(query), StandardCharsets.UTF_8);

        // https://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SearchObjectDto dto = objectMapper.readValue(decodedQuery, SearchObjectDto.class);

        log.info(dto.toString());
        Page<ContentNoBodyDto> contentPage = contentService.findBySearchObject(AuthorizationUtil.hasAdminRole(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(contentPage);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "글쓰기 시작전 새로운 content를 내려준다.")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("")
    public ResponseEntity newPost() {
        return ResponseEntity.status(HttpStatus.OK).body(contentService.newContent());
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 저장")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PutMapping("/{contentId}")
    public ResponseEntity savePost(@PathVariable int contentId, @Valid @RequestBody Content newContent, BindingResult bindingResult) {

        Content content = contentService.findById(contentId);
        if (content != null) {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError().getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body(contentService.update(content, newContent));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 포스트입니다.");
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "메인 포스트로 지정")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("/main/{contentId}")
    public ResponseEntity setMain(@PathVariable int contentId) {
        contentService.setMain(contentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 공개/비공개 설정")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("/public")
    public ResponseEntity changePublic(@RequestBody ContentPublicDto contentPublicDto) {
        Content content = contentService.findById(contentPublicDto.getId());
        content.setPublic(contentPublicDto.isPublicStatus());
        contentService.save(content);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{contentId}")
    public ResponseEntity deletePost(@PathVariable int contentId) {
        contentService.deleteById(contentId);
        return ResponseEntity.status(HttpStatus.OK).body(DeleteResponseDto.builder().id(String.valueOf(contentId)).build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트에 태그 추가")
    @ApiResponse(responseCode = "200")
    @PostMapping("/{contentId}/tag")
    public ResponseEntity addPostTag(@PathVariable int contentId, @RequestBody ContentTagDto contentTagDto) {
        Tag tag = tagService.save(Tag.builder().name(contentTagDto.getTagName().trim()).build());
        Content content = contentService.findById(contentId);
        content.addTag(tag);
        contentService.save(content);
        return ResponseEntity.status(HttpStatus.OK).body(tag);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트에 태그 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{contentId}/tag/{tagId}")
    public ResponseEntity deletePostTag(@PathVariable int contentId, @PathVariable int tagId) {
        Content content = contentService.findById(contentId);
        Tag tag = tagService.findById(tagId);
        content.removeTag(tag);
        contentService.save(content);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

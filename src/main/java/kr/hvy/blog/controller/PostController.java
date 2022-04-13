package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.entity.Content;
import kr.hvy.blog.model.base.Page;
import kr.hvy.blog.model.response.ContentNoBody;
import kr.hvy.blog.service.ContentService;
import kr.hvy.blog.util.AuthorizationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final ContentService contentService;

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


    @Operation(summary = "검색")
    @Parameters({
            @Parameter(name = "searchType", description = "TITLE / CONTENT / FULL(모두)", in = ParameterIn.QUERY),
            @Parameter(name = "searchText", description = "검색어( `|`, `&`) 사용가능 ", in = ParameterIn.QUERY),
            @Parameter(name = "categoryId", description = "카테고리 아이디", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지", example = "1", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "리스트 목록수", example = "10", in = ParameterIn.QUERY)
    })
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/search"})
    public ResponseEntity getMultipleList2(
            Authentication auth,
            @RequestParam(defaultValue = "TITLE") String searchType,
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "") String categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int pageSize) {
        Page<ContentNoBody> contentPage = contentService.findIdsByConditions(AuthorizationProvider.hasAdminRole(), searchType, searchText, categoryId, page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(contentPage);
    }

    @Operation(summary = "단일 포스트 조회")
    @ApiResponse(responseCode = "200",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
    )
    @GetMapping(value = {"/{contentId}"})
    public ResponseEntity getContent(@PathVariable int contentId) {
        return ResponseEntity.status(HttpStatus.OK).body(contentService.findById(contentId));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 저장")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("")
    public ResponseEntity savePost(@Valid @ModelAttribute Content content, BindingResult bindingResult) {
        throw new NotImplementedException("Not Implemented");

//        if (bindingResult.hasErrors()) {
//        			FieldError fieldError = bindingResult.getFieldError();
//
//        			MultipleResultSet category = categoryService.findCategoryWithProc();
//        			modelAndView.addObject("content", content);
//        			modelAndView.addObject("category", category.getTables().get("table0"));
//        			modelAndView.addObject("msg", fieldError.getDefaultMessage());
//        			modelAndView.addObject("stat", false);
//
//        			modelAndView.setViewName("admin/post/modify");
//        			return modelAndView;
//        		} else {
//        			try {
//        				contentService.save(content);
//        			} catch (Exception e) {
//        				MultipleResultSet category = categoryService.findCategoryWithProc();
//        				modelAndView.addObject("content", content);
//        				modelAndView.addObject("category", category.getTables().get("table0"));
//        				modelAndView.addObject("msg", "실패하였습니다.");
//        				modelAndView.addObject("stat", false);
//        				modelAndView.setViewName("admin/post/modify");
//        				return modelAndView;
//        			}
//
//        			// 저장이 완료되면 리스트로
//        			modelAndView.setViewName("redirect:/post/" + content.getId());
//        			return modelAndView;
//        		}

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "메인 포스트로 지정")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("/main")
    public ResponseEntity setMain() {
        // TODO: 메인으로 변경시에는 ID만 받는다
        throw new NotImplementedException("Not Implemented");

//        contentService.setMain(Integer.parseInt(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 공개/비공개 설정")
    @ApiResponse(responseCode = "200", content = {@io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = kr.hvy.blog.entity.Content.class))})
    @PostMapping("/public")
    public ResponseEntity changePublic() {
        // TODO: 공개로 변경시에는 ID와 공개여부를 받는다
        throw new NotImplementedException("Not Implemented");
//        Content con = contentService.findById(Integer.parseInt(id));
//        			con.setPublic(Boolean.parseBoolean(isPublic));
//        			contentService.save(con);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{contentId}")
    public ResponseEntity deletePost(@PathVariable int contentId) {
        throw new NotImplementedException("Not Implemented");
//        contentService.deleteById(Integer.parseInt(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트에 태그 추가")
    @ApiResponse(responseCode = "200")
    @PostMapping("/{contentId}/tag")
    public ResponseEntity addPostTag() {
        // TODO: ContentId와 TagId를 받는다.
        throw new NotImplementedException("Not Implemented");
//        contentService.deleteById(Integer.parseInt(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "포스트에 태그 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{contentId}/tag/{tagId}")
    public ResponseEntity deletePostTag() {
        // TODO: ContentId와 TagId를 받는다.
        throw new NotImplementedException("Not Implemented");
//        contentService.deleteById(Integer.parseInt(id));
    }


}

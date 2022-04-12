package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Category")
@RequestMapping("/api/category")
public class CategoryController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/root")
    public ResponseEntity getRootCategory() {
        // TODO: 루트를 가져온다 / 혹은 루트부터 쭈욱 다 뿌리는 식으로 변경할 수도 있다.
        throw new NotImplementedException("Not Implemented");
//        Category category = categoryService.findRoot();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        throw new NotImplementedException("Not Implemented");
//        category.cleanUp();
//        categoryService.saveWithProc(cate);
//        categoryService.updateFullName();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        throw new NotImplementedException("Not Implemented");
//        categoryService.deleteWithProc(id);
    }

}

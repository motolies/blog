package kr.hvy.blog.module.discount;

import java.util.Map;
import kr.hvy.blog.module.discount.code.DiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/discount")
public class DiscountController {

  private final Map<String, DiscountInterface> discountStrategy;

  @PostMapping
  public ResponseEntity<?> discountManually(@RequestBody DiscountType type) {

    discountStrategy.get(type.getBeanStyleHandlerName()).run();

    return ResponseEntity
        .status(HttpStatus.OK).build();
  }


}

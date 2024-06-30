package kr.hvy.blog.module.discount;

import java.util.List;
import kr.hvy.blog.module.discount.dto.DiscountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountRepository discountRepository;

  public List<DiscountInfo> savedDiscounts(List<String> discountIds) {
    return (List<DiscountInfo>) discountRepository.findAllById(discountIds);
  }

  public void save(DiscountInfo discount) {
    discountRepository.save(discount);
  }

}

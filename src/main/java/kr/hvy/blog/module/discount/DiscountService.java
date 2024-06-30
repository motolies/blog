package kr.hvy.blog.module.discount;

import java.util.List;
import kr.hvy.blog.module.discount.dto.Discount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountRepository discountRepository;

  public List<Discount> savedDiscounts(List<String> discountIds) {
    return (List<Discount>) discountRepository.findAllById(discountIds);
  }

  public void save(Discount discount) {
    discountRepository.save(discount);
  }

}

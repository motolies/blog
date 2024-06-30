package kr.hvy.blog.module.discount;

import kr.hvy.blog.module.discount.dto.Discount;
import org.springframework.data.repository.CrudRepository;


public interface DiscountRepository extends CrudRepository<Discount, String> {

}

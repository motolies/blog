package kr.hvy.blog.module.discount;

import java.util.List;
import java.util.function.Predicate;
import kr.hvy.blog.module.discount.dto.DiscountInfo;

public interface DiscountInterface {

  List<DiscountInfo> getList();

  Predicate<DiscountInfo> filtering();

  void run();

}

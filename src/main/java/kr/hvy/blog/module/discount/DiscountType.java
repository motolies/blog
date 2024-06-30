package kr.hvy.blog.module.discount;

import io.github.motolies.code.CommonEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType implements CommonEnumCode<String> {
  PPOMPPU("PPOMPPU", "뽐뿌", PpomppuHandler.class),
  CLIEN("CLIEN", "클리앙", null);

  private final String code;
  private final String desc;
  private final Class<? extends AbstractDiscountHandler> handler;

}

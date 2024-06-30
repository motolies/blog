package kr.hvy.blog.module.discount.code;

import io.github.motolies.code.CommonEnumCode;
import kr.hvy.blog.module.discount.AbstractDiscountHandler;
import kr.hvy.blog.module.discount.site.PpomppuHandler;
import kr.hvy.blog.module.discount.site.QuasarzoneHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum DiscountType implements CommonEnumCode<String> {
  PPOMPPU("PPOMPPU", "뽐뿌", PpomppuHandler.class),
  QUASARZONE("QUASARZONE", "퀘이사존", QuasarzoneHandler.class);

  private final String code;
  private final String desc;
  private final Class<? extends AbstractDiscountHandler> handler;

  public String getBeanStyleHandlerName() {
    return StringUtils.uncapitalize(handler.getSimpleName());
  }
}

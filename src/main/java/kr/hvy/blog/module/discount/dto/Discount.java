package kr.hvy.blog.module.discount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@RedisHash(value = "discountN", timeToLive = 60 * 60 * 24 * 3)
public class Discount {

  @Id
  private int redisKey;
  private String title;
  private String link;
  private int view;
  private int recommendUp;
  private int recommendDown;
  private int comment;

  public String getSlackMessage() {
    return String.format("%s\n%s", title, link);
  }

}

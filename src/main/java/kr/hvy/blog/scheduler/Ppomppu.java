package kr.hvy.blog.scheduler;

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
@RedisHash(value = "ppomppuN", timeToLive = 60 * 60 * 24 * 3)
public class Ppomppu {

  @Id
  private int seq;
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

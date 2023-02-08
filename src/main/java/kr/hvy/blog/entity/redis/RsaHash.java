package kr.hvy.blog.entity.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Builder
@RedisHash(value = "rsa")
public class RsaHash implements Serializable {
    private static final long serialVersionUID = 2163166868956980186L;

    @Id
    private String privateKey;
    @NonNull
    private String publicKey;

}

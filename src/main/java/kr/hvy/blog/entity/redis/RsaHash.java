package kr.hvy.blog.entity.redis;

import kr.hvy.blog.util.Base64WebSafeUtil;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

@Getter
@RedisHash(value = "rsa")
public class RsaHash implements Serializable {
    private static final long serialVersionUID = 2163166868956980186L;

    @Id
    private String publicKey;
    private String privateKey;

    @Builder
    public RsaHash(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

//    public String getPublicKey() throws UnsupportedEncodingException {
//        return Base64WebSafeUtil.encode(publicKey);
//    }
//
//    public String getPrivateKey() throws UnsupportedEncodingException {
//        return Base64WebSafeUtil.encode(privateKey);
//    }
}

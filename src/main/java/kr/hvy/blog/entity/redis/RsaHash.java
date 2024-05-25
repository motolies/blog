package kr.hvy.blog.entity.redis;

import java.io.Serial;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Base64;

@Getter
@RedisHash(value = "rsa")
@ToString
public class RsaHash implements Serializable {
    @Serial
    private static final long serialVersionUID = 2163166868956980186L;

    @Id
    private String publicKey;
    private String privateKey;

    @Builder
    public RsaHash(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public byte[] getPrivateKeyBytes() {
        return Base64.getDecoder().decode(privateKey);
    }

//    public String getPublicKey() throws UnsupportedEncodingException {
//        return Base64WebSafeUtil.encode(publicKey);
//    }
//
//    public String getPrivateKey() throws UnsupportedEncodingException {
//        return Base64WebSafeUtil.encode(privateKey);
//    }
}

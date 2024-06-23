package kr.hvy.blog.module.auth;

import jakarta.annotation.PostConstruct;
import java.util.Optional;
import kr.hvy.blog.module.auth.domain.RsaHash;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RsaHashService {

    private final RsaHashRepository rsaHashRepository;

    private final RedisTemplate redisTemplateString;

    @PostConstruct
    public void init() {
        setOps = redisTemplateString.opsForSet();
    }

    private SetOperations<String, String> setOps;


    public void save(RsaHash hash) {
        rsaHashRepository.save(hash);
    }

    public Optional<RsaHash> findByPublicKey(String publicKey) {
        return rsaHashRepository.findById(publicKey);
    }


    public RsaHash random() {
        return rsaHashRepository.findById(randomKey())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키입니다."));
    }

    private String randomKey() {
        return setOps.randomMember("rsa");
    }


}

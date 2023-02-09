package kr.hvy.blog.service;

import kr.hvy.blog.entity.redis.RsaHash;
import kr.hvy.blog.repository.RsaHashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

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
        return rsaHashRepository.findById(randomKey()).orElse(null);
    }

    private String randomKey() {
        return setOps.randomMember("rsa");
    }


}

package kr.hvy.blog.config;

import kr.hvy.blog.repository.RsaHashRepository;
import kr.hvy.blog.security.RSAEncryptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Configuration
public class RsaKeyInit {
    private final RsaHashRepository rsaHashRepository;

    // TODO : init 시에 100개를 만드니까 너무 오래 걸리는 것 같다.

    @PostConstruct
    public void RandomKeyInit() throws NoSuchAlgorithmException {
        for (int i = 0; i < 100; i++){
            rsaHashRepository.save(RSAEncryptHelper.makeRsaHash());
        }
    }
}

package kr.hvy.blog.config;

import kr.hvy.blog.repository.RsaHashRepository;
import kr.hvy.blog.security.RSAEncryptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Configuration
public class RsaKeyInit {
    private final RsaHashRepository rsaHashRepository;

    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @PostConstruct
    public void RandomKeyInit() throws NoSuchAlgorithmException {
        final List<CompletableFuture> futures = IntStream.rangeClosed(1, 100)
                .boxed()
                .map(i -> CompletableFuture.supplyAsync(() -> {
                            try {
                                rsaHashRepository.save(RSAEncryptHelper.makeRsaHash());
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }, executor)
                )
                .collect(Collectors.toList());

        futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}

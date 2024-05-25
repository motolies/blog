package kr.hvy.blog.config;

import jakarta.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kr.hvy.blog.security.RSAEncryptHelper;
import kr.hvy.blog.service.RsaHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@RequiredArgsConstructor
@Configuration
public class RsaKeyInit {

  private final RsaHashService rsaHashService;

  @Qualifier("taskExecutor")
  private final TaskExecutor taskExecutor;

  @PostConstruct
  public void RandomKeyInit() throws NoSuchAlgorithmException {
    final List<CompletableFuture> futures = IntStream.rangeClosed(1, 100)
        .boxed()
        .map(i -> CompletableFuture.supplyAsync(() -> {
              try {
                rsaHashService.save(RSAEncryptHelper.makeRsaHash());
              } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
              }
              return null;
            }, taskExecutor)
        )
        .collect(Collectors.toList());

    futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }
}

package kr.hvy.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"kr.hvy.blog", "io.github.motolies.aop.log"})
@MapperScan("kr.hvy.blog.mapper")
@EnableJpaRepositories(basePackages = {"kr.hvy.blog", "io.github.motolies.aop.log"})
@EntityScan(basePackages = {"kr.hvy.blog", "io.github.motolies.aop.log"})
@EnableAsync
public class SkyscapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyscapeApplication.class, args);
	}

}

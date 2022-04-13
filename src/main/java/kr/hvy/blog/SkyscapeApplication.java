package kr.hvy.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("kr.hvy.blog.mapper")
public class SkyscapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyscapeApplication.class, args);
	}

}

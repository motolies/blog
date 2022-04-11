package kr.hvy.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // https://springdoc.org/faq.html
        // 함수별로 지정할 수 있다.
        // 단 함수의 @Operation 에 아래와 같이 추가해줘야 한다.
        // @Operation(summary = "함수 설명입니다.", security = {@SecurityRequirement(name = "bearer-key")})
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}

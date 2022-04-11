package kr.hvy.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SpringDocConfig {

//    @Bean
//    public OpenAPI customOpenAPI() {
//        // https://springdoc.org/faq.html
//        // 함수별로 지정할 수 있다.
//        // 단 함수의 @Operation 에 아래와 같이 추가해줘야 한다.
//        // @Operation(summary = "함수 설명입니다.", security = {@SecurityRequirement(name = "bearer-key")})
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
//    }

    @Bean
    public OpenAPI customOpenAPI() {
        // https://stackoverflow.com/questions/70855605/how-to-ask-swagger-ui-to-add-bearer-token-field-for-each-endpoint-globally
        // 전역으로 할 수 있는 자물쇠를 사용한다
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("Auth Service API")
                        .description("Documentation of API v.1.0")
                        .version("1.0")
                ).addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearer-jwt", Arrays.asList("read", "write"))
                                .addList("bearer-key", Collections.emptyList())
                );
    }

}

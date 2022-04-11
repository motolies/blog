package kr.hvy.blog.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan("kr.hvy.blog.mapper")
public class MyBatisConfig {


    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        // https://lob-dev.tistory.com/entry/Somaeja-%EC%86%8C%EB%A7%A4%EC%9E%90-03-Spring-Boot-Mybatis-%EC%84%A4%EC%A0%95-%EB%AC%B8%EC%A0%9C-20201202

        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis-mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }


}

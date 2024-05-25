package kr.hvy.blog.config;

import io.github.motolies.config.TaskExecutorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Profile("!test")
@Configuration
@EnableAsync
public class TaskExecutorConfig extends TaskExecutorConfigurer {

  @Bean(name = "taskExecutor")
  public TaskExecutor taskExecutor() {
    return taskExecutor(20, 50, 10000, "AsyncTask-");
  }
}

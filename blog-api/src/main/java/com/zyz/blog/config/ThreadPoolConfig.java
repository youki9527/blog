package com.zyz.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zyz
 * @version 1.0
 *
 * 线程池
 *
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {


	@Bean("taskExecutor")
	public Executor asyncServiceExecutor(){
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

		threadPoolTaskExecutor.setCorePoolSize(5);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);

		threadPoolTaskExecutor.setKeepAliveSeconds(60);
		threadPoolTaskExecutor.setThreadNamePrefix("zyz博客项目");

		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

}

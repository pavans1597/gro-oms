package com.groyyo.order.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfiguration {

	@Value("${thread.pool.size:6}")
	private int corePoolSize;

	@Value("${thread.max.pool.size:12}")
	private int maxPoolSize;

	@Value("${thread.keepAliveSeconds:30}")
	private int keepAliveSeconds;

	@Value("${thread.queue.capacity:100}")
	private int queueCapacity;

	@Bean(name = "orderThreadExecutor")
	public ThreadPoolTaskExecutor orderThreadExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);
		taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

		return taskExecutor;
	}

}
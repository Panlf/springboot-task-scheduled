package com.plf.task.scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.extern.slf4j.Slf4j;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@Slf4j
@EnableAsync
@EnableScheduling
public class SpringbootTaskScheduledApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTaskScheduledApplication.class, args);
	}
	
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(20);
		threadPoolTaskScheduler.setThreadNamePrefix("threadPoolTaskScheduler-");
		threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
		log.info("已经载入ThreadPoolTaskScheduler的Bean对象,对象为{}",threadPoolTaskScheduler);
		return threadPoolTaskScheduler;
	}

	/**
	 * 解决url传参存在特殊字符
	 * @return
	 */
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
		return factory;
	}
}

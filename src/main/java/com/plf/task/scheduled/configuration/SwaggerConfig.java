package com.plf.task.scheduled.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//开启Swagger
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.apiInfo(apiInfo())
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.plf.task.scheduled"))
					.paths(PathSelectors.any())
					.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("动态增加、修改、暂停定时任务")
				.description("基于SpringBoot和Spring自带的scheduled的定时任务，可以动态修改、删除、增加定时任务。")
				.termsOfServiceUrl("https://github.com/Panlf/springboot-task-scheduled")
				.version("0.0.1").build();
	}
}

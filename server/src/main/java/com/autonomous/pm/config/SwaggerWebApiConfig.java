package com.autonomous.pm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
//@Profile({"local","dev"})
public class SwaggerWebApiConfig {

	@Bean
	public Docket swagger() {

		/* Set Global API Key */
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.autonomous.pm"))
					.paths(PathSelectors.any())
					.build()
				.produces(getAllProduceContentTypes())	// Request headers { Accept: application/json; }
				.consumes(getAllConsumeContentTypes())	// Request headers { Content-Type: application/json; }
				.securitySchemes(Arrays.asList(apiKey()));

	}
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Automous PM Server - API List")
                .version("1.0.0")
                .build();
    }
    
    private ApiKey apiKey() {
        return new ApiKey(
        		"Authorization",
        		"PM-AS-KEY",
        		"header"
        		);
    }
    
    private Set<String> getAllProduceContentTypes(){
    	Set<String> produces = new HashSet<String>();
    	produces.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
    	return produces;
    }
    
    private Set<String> getAllConsumeContentTypes(){
    	Set<String> consumes = new HashSet<String>();
    	consumes.add(MediaType.APPLICATION_JSON_VALUE);
    	return consumes;
    }

}

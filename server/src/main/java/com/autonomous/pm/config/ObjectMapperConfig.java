package com.autonomous.pm.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.autonomous.pm.component.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {

	@Lazy
	@Autowired
	ObjectMapper mapper;
	
//	@Bean
//	public ObjectMapper configureMapper() {
//		// https://pasudo123.tistory.com/362
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		
//    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//    	sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//    	mapper.setDateFormat(sdf);
//
//			
//		return new ObjectMapper();
//	}
	
//	@Bean
//	@Primary
//	public ObjectMapper objectMapper() {
//		return new Jackson2ObjectMapperBuilder()
//				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//				.modules(new JavaTimeModule())
//				.timeZone("Asia/Seoul")
//				.build();
//	}

//	@Bean
//	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//		return new Jackson2ObjectMapperBuilderCustomizer() {
//			@Override
//			public void customize(Jackson2ObjectMapperBuilder builder) {
//				builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//				builder.timeZone(TimeZone.getTimeZone("UTC"));
//			}
//		};
//	}

	@Bean
	public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
		ObjectMapper copy = mapper.copy();
		copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
		return new MappingJackson2HttpMessageConverter(copy);
	}
	
}

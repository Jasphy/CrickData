package com.web.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Medhassu2Application {
	
	@Value("${spring.jpa.properties.hibernate.dialect}")
	public static String prop;

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
	            mapper);
	    System.out.println(prop);
	    return converter;
	}
	public static void main(String[] args) {
		
		SpringApplication.run(Medhassu2Application.class, args);
	}
}

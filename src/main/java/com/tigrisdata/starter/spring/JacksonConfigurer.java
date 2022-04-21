package com.tigrisdata.starter.spring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/** This is to customize default ObjectMapper used by spring-mvc. */
@Configuration
public class JacksonConfigurer implements Jackson2ObjectMapperBuilderCustomizer {
  /**
   * Customization here enables jackson to work with immutable objects
   *
   * @param builder ongoing builder
   */
  @Override
  public void customize(Jackson2ObjectMapperBuilder builder) {
    builder.modules(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }
}

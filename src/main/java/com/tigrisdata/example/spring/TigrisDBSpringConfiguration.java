package com.tigrisdata.example.spring;

import com.tigrisdata.db.client.auth.TigrisAuthorizationToken;
import com.tigrisdata.db.client.config.TigrisDBConfiguration;
import com.tigrisdata.db.client.service.StandardTigrisDBClient;
import com.tigrisdata.db.client.service.TigrisDBClient;
import com.tigrisdata.db.client.service.TigrisDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TigrisDBSpringConfiguration {

  @Bean
  public TigrisDatabase tigrisDatabase(
      @Value("${tigrisdb.db.name}") String dbName, TigrisDBClient client) {
    return client.getDatabase(dbName);
  }

  @Bean
  public TigrisDBConfiguration tigrisDBConfiguration(
      @Value("${tigrisdb.server.url}") String baseUrl,
      @Value("${tigrisdb.network.usePlainText:false}") boolean usePlainText) {
    TigrisDBConfiguration.NetworkConfig.NetworkConfigBuilder networkConfigBuilder =
        TigrisDBConfiguration.NetworkConfig.newBuilder();
    if (usePlainText) {
      networkConfigBuilder.usePlainText();
    }
    return TigrisDBConfiguration.newBuilder(baseUrl)
        .withNetwork(networkConfigBuilder.build())
        .build();
  }

  @Bean
  public TigrisDBClient tigrisDBClient(
      @Value("${tigrisdb.authorization.token}") String token,
      TigrisDBConfiguration tigrisDBConfiguration) {
    return StandardTigrisDBClient.getInstance(
        new TigrisAuthorizationToken(token), tigrisDBConfiguration);
  }

  @Bean
  public TigrisDBInitializer tigrisDBInitializr(
      TigrisDBClient tigrisDBClient, @Value("${tigrisdb.db.name}") String dbName) {
    return new TigrisDBInitializer(tigrisDBClient, dbName);
  }
}

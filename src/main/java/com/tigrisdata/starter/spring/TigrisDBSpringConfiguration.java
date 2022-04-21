/*
 * Copyright 2022 Tigris Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tigrisdata.starter.spring;

import com.tigrisdata.db.client.StandardTigrisDBClient;
import com.tigrisdata.db.client.TigrisDBClient;
import com.tigrisdata.db.client.TigrisDatabase;
import com.tigrisdata.db.client.auth.TigrisAuthorizationToken;
import com.tigrisdata.db.client.config.TigrisDBConfiguration;
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

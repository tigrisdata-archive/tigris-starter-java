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

import com.tigrisdata.db.client.StandardTigrisClient;
import com.tigrisdata.db.client.TigrisClient;
import com.tigrisdata.db.client.TigrisDatabase;
import com.tigrisdata.db.client.config.TigrisConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class TigrisSpringConfiguration {

  private static final Logger log = LoggerFactory.getLogger(TigrisSpringConfiguration.class);

  @Bean
  public TigrisDatabase tigrisDatabase(
      @Value("${tigris.db.name}") String dbName, TigrisClient client) {
    return client.getDatabase(dbName);
  }

  @Bean
  public TigrisClient tigrisClient(
      @Value("${tigris.server.url}") String serverURL,
      @Value("${tigris.network.usePlainText:false}") boolean usePlainText,
      @Value("${tigris.auth.appId:#{null}}") Optional<String> applicationId,
      @Value("${tigris.auth.appSecret:#{null}}") Optional<String> applicationSecret) {

    log.info("Initializing tigris client with server.url={}", serverURL);

    TigrisConfiguration.Builder configBuilder = TigrisConfiguration.newBuilder(serverURL);

    if (usePlainText) {
      // no TLS
      configBuilder.withNetwork(
          TigrisConfiguration.NetworkConfig.newBuilder().usePlainText().build());
    }

    if (applicationId.isPresent() && applicationSecret.isPresent()) {
      configBuilder.withAuthConfig(
          new TigrisConfiguration.AuthConfig(applicationId.get(), applicationSecret.get()));
    }

    return StandardTigrisClient.getInstance(configBuilder.build());
  }

  @Bean
  public TigrisInitializer tigrisInitializr(
      TigrisClient tigrisClient, @Value("${tigris.db.name}") String dbName) {
    return new TigrisInitializer(tigrisClient, dbName);
  }
}

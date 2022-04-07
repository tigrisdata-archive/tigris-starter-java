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

import com.tigrisdata.db.client.model.DatabaseOptions;
import com.tigrisdata.db.client.service.TigrisDBClient;
import com.tigrisdata.db.client.service.TigrisDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.io.File;

public class TigrisDBInitializer implements CommandLineRunner {

  private final TigrisDBClient tigrisDBClient;
  private final String dbName;

  private static final Logger log = LoggerFactory.getLogger(TigrisDBInitializer.class);

  public TigrisDBInitializer(TigrisDBClient tigrisDBClient, String dbName) {
    this.tigrisDBClient = tigrisDBClient;
    this.dbName = dbName;
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("createDbIfNotExists db: {}", dbName);
    tigrisDBClient.createDatabaseIfNotExists(dbName, DatabaseOptions.DEFAULT_INSTANCE);

    log.info("creating collections on db {}", dbName);
    TigrisDatabase tigrisDatabase = tigrisDBClient.getDatabase(dbName);
    tigrisDatabase.createCollectionsInTransaction(new File("src/main/resources/tigrisdb-schema"));
    log.info("Finished initializing TigrisDB");
  }
}

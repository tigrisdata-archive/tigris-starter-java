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

import com.tigrisdata.db.client.TigrisClient;
import com.tigrisdata.db.client.TigrisDatabase;
import com.tigrisdata.starter.collections.Order;
import com.tigrisdata.starter.collections.Product;
import com.tigrisdata.starter.collections.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class TigrisInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(TigrisInitializer.class);
  private final TigrisClient tigrisClient;
  private final String dbName;

  public TigrisInitializer(TigrisClient tigrisClient, String dbName) {
    this.tigrisClient = tigrisClient;
    this.dbName = dbName;
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("createDbIfNotExists db: {}", dbName);
    TigrisDatabase tigrisDatabase = tigrisClient.createDatabaseIfNotExists(dbName);
    log.info("creating collections on db {}", dbName);
    tigrisDatabase.createOrUpdateCollections(User.class, Product.class, Order.class);

    // stream changes as they happen on orders collection
    tigrisDatabase
        .getCollection(Order.class)
        .events()
        .forEachRemaining(
            streamEvent -> {
              log.info(
                  "CHANGES: collection={}, operation={}, data={}, txId={}",
                  streamEvent.getCollection(),
                  streamEvent.getOp(),
                  streamEvent.getData(),
                  streamEvent.getTxId());
            });

    log.info("Finished initializing Tigris");
  }
}

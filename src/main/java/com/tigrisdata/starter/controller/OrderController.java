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
package com.tigrisdata.starter.controller;

import com.tigrisdata.db.client.Filters;
import com.tigrisdata.db.client.TigrisCollection;
import com.tigrisdata.db.client.TigrisDatabase;
import com.tigrisdata.db.client.error.TigrisException;
import com.tigrisdata.starter.collections.Order;
import com.tigrisdata.starter.collections.Product;
import com.tigrisdata.starter.collections.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("order")
public class OrderController {
  private final TigrisDatabase tigrisStarterDatabase;
  private final TigrisCollection<User> userCollection;
  private final TigrisCollection<Product> productCollection;
  private final TigrisCollection<Order> orderCollection;

  public OrderController(TigrisDatabase tigrisStarterDatabase) {
    this.tigrisStarterDatabase = tigrisStarterDatabase;
    this.userCollection = tigrisStarterDatabase.getCollection(User.class);
    this.productCollection = tigrisStarterDatabase.getCollection(Product.class);
    this.orderCollection = tigrisStarterDatabase.getCollection(Order.class);
  }

  @GetMapping("/read/{id}")
  public ResponseEntity<Order> read(@PathVariable("id") int id) throws TigrisException {
    Optional<Order> order =
        tigrisStarterDatabase.getCollection(Order.class).readOne(Filters.eq("id", id));
    return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}

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

import com.tigrisdata.db.client.error.TigrisDBException;
import com.tigrisdata.db.client.model.Filters;
import com.tigrisdata.db.client.model.ReadFields;
import com.tigrisdata.db.client.model.TransactionOptions;
import com.tigrisdata.db.client.model.UpdateFields;
import com.tigrisdata.db.client.service.TigrisDatabase;
import com.tigrisdata.db.client.service.TransactionSession;
import com.tigrisdata.db.client.service.TransactionTigrisCollection;
import com.tigrisdata.starter.generated.Order;
import com.tigrisdata.starter.generated.Product;
import com.tigrisdata.starter.generated.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("shop")
public class OrderController {
  private final TigrisDatabase tigrisStarterDatabase;
  private final AtomicLong orderIdSequence;

  public OrderController(TigrisDatabase tigrisStarterDatabase) {
    this.tigrisStarterDatabase = tigrisStarterDatabase;
    this.orderIdSequence = new AtomicLong();
  }

  @PostMapping("{user_id}/{product_id}/{qty}")
  public ResponseEntity<String> purchase(
      @RequestParam("user_id") long userId,
      @RequestParam("product_id") long productId,
      @RequestParam("qty") int quantity)
      throws TigrisDBException {
    TransactionSession transactionSession =
        tigrisStarterDatabase.beginTransaction(new TransactionOptions());
    try {
      TransactionTigrisCollection<User> userCollection =
          transactionSession.getCollection(User.class);
      TransactionTigrisCollection<Product> productCollection =
          transactionSession.getCollection(Product.class);
      TransactionTigrisCollection<Order> orderCollection =
          transactionSession.getCollection(Order.class);

      User user = userCollection.readOne(Filters.eq("id", userId)).get();
      Product product =
          productCollection.read(Filters.eq("id", productId), ReadFields.empty()).next();

      if (product.getQuantity() >= quantity && product.getPrice() * quantity <= user.getBalance()) {
        double orderTotal = product.getPrice() * quantity;
        double newUserBalance = user.getBalance() - orderTotal;
        int newProductQuantity = product.getQuantity() - quantity;
        userCollection.update(
            Filters.eq("id", userId),
            UpdateFields.newBuilder()
                .set(UpdateFields.SetFields.newBuilder().set("balance", newUserBalance).build())
                .build());

        productCollection.update(
            Filters.eq("id", productId),
            UpdateFields.newBuilder()
                .set(
                    UpdateFields.SetFields.newBuilder().set("quantity", newProductQuantity).build())
                .build());

        orderCollection.insert(
            new Order()
                .withId(orderIdSequence.incrementAndGet())
                .withOrderTotal(orderTotal)
                .withUserId(userId)
                .withProductId(productId));

        transactionSession.commit();
        return ResponseEntity.status(HttpStatus.OK).body("Purchased successfully");
      } else {
        transactionSession.rollback();
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Not enough balance");
      }
    } catch (Exception ex) {
      transactionSession.rollback();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to shop");
    }
  }
}

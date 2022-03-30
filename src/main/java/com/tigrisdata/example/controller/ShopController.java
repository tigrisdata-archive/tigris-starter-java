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
package com.tigrisdata.example.controller;

import com.tigrisdata.db.client.error.TigrisDBException;
import com.tigrisdata.db.client.model.Fields;
import com.tigrisdata.db.client.model.Filters;
import com.tigrisdata.db.client.model.TransactionOptions;
import com.tigrisdata.db.client.service.TigrisDatabase;
import com.tigrisdata.db.client.service.TransactionSession;
import com.tigrisdata.db.client.service.TransactionTigrisCollection;
import com.tigrisdata.store.generated.Product;
import com.tigrisdata.store.generated.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("shop")
public class ShopController {
  private final TigrisDatabase tigrisStoreDatabase;

  public ShopController(TigrisDatabase tigrisStoreDatabase) {
    this.tigrisStoreDatabase = tigrisStoreDatabase;
  }

  @PostMapping("{user_id}/{product_id}/{qty}")
  public ResponseEntity<String> purchase(
      @RequestParam("user_id") int userId,
      @RequestParam("product_id") int productId,
      @RequestParam("qty") int quantity)
      throws TigrisDBException {
    TransactionSession transactionSession =
        tigrisStoreDatabase.beginTransaction(new TransactionOptions());
    try {
      TransactionTigrisCollection<User> userCollection =
          transactionSession.getCollection(User.class);
      TransactionTigrisCollection<Product> productCollection =
          transactionSession.getCollection(Product.class);

      User user = userCollection.read(Filters.eq("id", userId), Collections.emptyList()).next();
      Product product =
          productCollection.read(Filters.eq("id", productId), Collections.emptyList()).next();

      if (product.getQuantity() >= quantity && product.getPrice() * quantity <= user.getBalance()) {
        double newUserBalance = user.getBalance() - product.getPrice() * quantity;
        int newProductQuantity = product.getQuantity() - quantity;
        userCollection.update(
            Filters.eq("id", userId),
            Collections.singletonList(Fields.doubleField("balance", newUserBalance)));

        productCollection.update(
            Filters.eq("id", productId),
            Collections.singletonList(Fields.integerField("quantity", newProductQuantity)));

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

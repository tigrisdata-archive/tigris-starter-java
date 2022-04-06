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
import com.tigrisdata.starter.generated.*;

import com.tigrisdata.db.client.service.TigrisCollection;
import com.tigrisdata.db.client.service.TigrisDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
public class ProductController {

  private final TigrisCollection<Product> productTigrisCollection;

  public ProductController(TigrisDatabase tigrisDatabase) {
    this.productTigrisCollection = tigrisDatabase.getCollection(Product.class);
  }

  @PostMapping("/create")
  public ResponseEntity<String> create(@RequestBody Product product) throws TigrisDBException {
    productTigrisCollection.insert(product);
    return ResponseEntity.status(HttpStatus.CREATED).body("product created");
  }

  @GetMapping("/{id}")
  public Product read(@PathVariable("id") int id) throws TigrisDBException {
    return productTigrisCollection.readOne(Filters.eq("id", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") int id) throws TigrisDBException {
    productTigrisCollection.delete(Filters.eq("id", id));
    return ResponseEntity.status(HttpStatus.OK).body("product deleted");
  }
}

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
import com.tigrisdata.db.client.search.SearchRequestOptions;
import com.tigrisdata.db.client.search.SearchResult;
import com.tigrisdata.starter.ConversionUtil;
import com.tigrisdata.starter.collections.Product;
import com.tigrisdata.starter.models.SearchRequest;
import java.util.Optional;
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
  public ResponseEntity<String> create(@RequestBody Product product) throws TigrisException {
    productTigrisCollection.insert(product);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("product created with id = " + product.getId());
  }

  @GetMapping("/read/{id}")
  public ResponseEntity<Product> read(@PathVariable("id") int id) throws TigrisException {
    Optional<Product> product = productTigrisCollection.readOne(Filters.eq("id", id));
    return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/search")
  public ResponseEntity<SearchResult<Product>> search(@RequestBody SearchRequest request)
      throws TigrisException {
    Optional<SearchResult<Product>> result = productTigrisCollection.search(
        ConversionUtil.toInternalSearchRequest(request),
        SearchRequestOptions.getDefault());

    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") int id) throws TigrisException {
    productTigrisCollection.delete(Filters.eq("id", id));
    return ResponseEntity.status(HttpStatus.OK).body("product deleted");
  }
}

package com.tigrisdata.example.controller;

import com.tigrisdata.db.client.error.TigrisDBException;
import com.tigrisdata.db.client.model.Filters;
import com.tigrisdata.db.client.service.TigrisCollection;
import com.tigrisdata.db.client.service.TigrisDatabase;
import com.tigrisdata.store.generated.Product;
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

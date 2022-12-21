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
package com.tigrisdata.starter.collections;

import com.tigrisdata.db.annotation.TigrisCollection;
import com.tigrisdata.db.annotation.TigrisField;
import com.tigrisdata.db.annotation.TigrisPrimaryKey;
import com.tigrisdata.db.type.TigrisCollectionType;

import java.util.Objects;

@TigrisCollection("product_collection")
public class Product implements TigrisCollectionType {

  @TigrisField(description = "A unique identifier for the product")
  @TigrisPrimaryKey(order = 1, autoGenerate = true)
  private int id;

  @TigrisField(description = "Name of the product")
  private String name;

  @TigrisField(description = "Number of products available in the store")
  private int quantity;

  @TigrisField(description = "Price of the product")
  private double price;

  public Product() {}

  public Product(int id, String name, int quantity, double price) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return quantity == product.quantity
        && Double.compare(product.price, price) == 0
        && Objects.equals(id, product.id)
        && Objects.equals(name, product.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, quantity, price);
  }
}

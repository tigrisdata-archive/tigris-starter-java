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

import com.tigrisdata.db.annotation.TigrisDBCollectionField;
import com.tigrisdata.db.annotation.TigrisDBCollectionPrimaryKey;
import com.tigrisdata.db.type.TigrisCollectionType;

import java.util.Objects;

public class Product implements TigrisCollectionType {

  @TigrisDBCollectionField(description = "A unique identifier for the product")
  @TigrisDBCollectionPrimaryKey(1)
  private final int id;

  @TigrisDBCollectionField(description = "Name of the product")
  private final String name;

  @TigrisDBCollectionField(description = "Number of products available in the store")
  private final int quantity;

  @TigrisDBCollectionField(description = "Price of the product")
  private final double price;

  public Product(int id, String name, int quantity, double price) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Product product = (Product) o;

    if (id != product.id) return false;
    if (quantity != product.quantity) return false;
    if (Double.compare(product.price, price) != 0) return false;
    return Objects.equals(name, product.name);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + quantity;
    temp = Double.doubleToLongBits(price);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}

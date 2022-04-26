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

import com.tigrisdata.db.annotation.TigrisField;
import com.tigrisdata.db.annotation.TigrisPrimaryKey;
import com.tigrisdata.db.type.TigrisCollectionType;

import java.util.List;
import java.util.Objects;

public class Order implements TigrisCollectionType {

  @TigrisField(description = "A unique identifier for the order")
  @TigrisPrimaryKey(1)
  private int id;

  @TigrisField(description = "The identifier of the user that placed the order")
  private int userId;

  @TigrisField(description = "The total cost of the order")
  private double orderTotal;

  @TigrisField(description = "The list of products that are part of this order")
  private List<ProductItem> productItems;

  public Order() {}

  public Order(int id, int userId, double orderTotal, List<ProductItem> productItems) {
    this.id = id;
    this.userId = userId;
    this.orderTotal = orderTotal;
    this.productItems = productItems;
  }

  public static class ProductItem {
    @TigrisField(description = "The product identifier")
    private int productId;

    @TigrisField(description = "The quantity of this product in this order")
    private int quantity;

    public ProductItem() {}

    public ProductItem(int productId, int quantity) {
      this.productId = productId;
      this.quantity = quantity;
    }

    public int getProductId() {
      return productId;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setProductId(int productId) {
      this.productId = productId;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ProductItem that = (ProductItem) o;

      if (productId != that.productId) return false;
      return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
      int result = productId;
      result = 31 * result + quantity;
      return result;
    }
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public double getOrderTotal() {
    return orderTotal;
  }

  public List<ProductItem> getProductItems() {
    return productItems;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setOrderTotal(double orderTotal) {
    this.orderTotal = orderTotal;
  }

  public void setProductItems(List<ProductItem> productItems) {
    this.productItems = productItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return id == order.id
        && userId == order.userId
        && Double.compare(order.orderTotal, orderTotal) == 0
        && Objects.equals(productItems, order.productItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, orderTotal, productItems);
  }
}

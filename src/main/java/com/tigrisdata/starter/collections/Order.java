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

import java.util.List;
import java.util.Objects;

public class Order implements TigrisCollectionType {

  @TigrisDBCollectionField(description = "A unique identifier for the order")
  @TigrisDBCollectionPrimaryKey(1)
  private final int id;

  @TigrisDBCollectionField(description = "The identifier of the user that placed the order")
  private final int userId;

  @TigrisDBCollectionField(description = "The total cost of the order")
  private final double orderTotal;

  @TigrisDBCollectionField(description = "The list of products that are part of this order")
  private final List<ProductItem> productItems;

  public Order(int id, int userId, double orderTotal, List<ProductItem> productItems) {
    this.id = id;
    this.userId = userId;
    this.orderTotal = orderTotal;
    this.productItems = productItems;
  }

  public static class ProductItem {
    @TigrisDBCollectionField(description = "The product identifier")
    private final int productId;

    @TigrisDBCollectionField(description = "The quantity of this product in this order")
    private final int quantity;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Order order = (Order) o;

    if (id != order.id) return false;
    if (userId != order.userId) return false;
    if (Double.compare(order.orderTotal, orderTotal) != 0) return false;
    return Objects.equals(productItems, order.productItems);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + userId;
    temp = Double.doubleToLongBits(orderTotal);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (productItems != null ? productItems.hashCode() : 0);
    return result;
  }
}

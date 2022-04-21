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

public class User implements TigrisCollectionType {
  @TigrisDBCollectionField(description = "A unique identifier for the user")
  @TigrisDBCollectionPrimaryKey(1)
  private final int id;

  @TigrisDBCollectionField(description = "Name of the user")
  private final String name;

  @TigrisDBCollectionField(description = "User account balance")
  private final double balance;

  public User(int id, String name, double balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getBalance() {
    return balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (id != user.id) return false;
    if (Double.compare(user.balance, balance) != 0) return false;
    return Objects.equals(name, user.name);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    temp = Double.doubleToLongBits(balance);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}

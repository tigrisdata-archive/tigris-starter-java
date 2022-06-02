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
import com.tigrisdata.db.client.InsertResponse;
import com.tigrisdata.db.client.TigrisCollection;
import com.tigrisdata.db.client.TigrisDatabase;
import com.tigrisdata.db.client.error.TigrisException;
import com.tigrisdata.starter.collections.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

  private final TigrisCollection<User> userTigrisCollection;

  public UserController(TigrisDatabase tigrisDatabase) {
    this.userTigrisCollection = tigrisDatabase.getCollection(User.class);
  }

  @PostMapping("/create")
  public ResponseEntity<String> create(@RequestBody User user) throws TigrisException {
    InsertResponse<User> insertResponse = userTigrisCollection.insert(user);
    return ResponseEntity.status(HttpStatus.CREATED).body("User created with id = "+insertResponse.getGeneratedKeys()[0].get("id"));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> read(@PathVariable("id") int id) throws TigrisException {
    Optional<User> user = userTigrisCollection.readOne(Filters.eq("id", id));
    if (user.isPresent()) {
      return ResponseEntity.ok(user.get());
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") int id) throws TigrisException {
    userTigrisCollection.delete(Filters.eq("id", id));
    return ResponseEntity.status(HttpStatus.OK).body("User deleted");
  }
}

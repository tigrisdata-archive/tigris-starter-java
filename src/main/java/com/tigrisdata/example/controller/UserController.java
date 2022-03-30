package com.tigrisdata.example.controller;

import com.tigrisdata.db.client.error.TigrisDBException;
import com.tigrisdata.db.client.model.Filters;
import com.tigrisdata.db.client.service.TigrisCollection;
import com.tigrisdata.db.client.service.TigrisDatabase;
import com.tigrisdata.store.generated.User;
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
@RequestMapping("users")
public class UserController {

  private final TigrisCollection<User> userTigrisCollection;

  public UserController(TigrisDatabase tigrisDatabase) {
    this.userTigrisCollection = tigrisDatabase.getCollection(User.class);
  }

  @PostMapping("/create")
  public ResponseEntity<String> create(@RequestBody User user) throws TigrisDBException {
    userTigrisCollection.insert(user);
    return ResponseEntity.status(HttpStatus.CREATED).body("User created");
  }

  @GetMapping("/{id}")
  public User read(@PathVariable("id") int id) throws TigrisDBException {
    return userTigrisCollection.readOne(Filters.eq("id", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") int id) throws TigrisDBException {
    userTigrisCollection.delete(Filters.eq("id", id));
    return ResponseEntity.status(HttpStatus.OK).body("User deleted");
  }
}

package com.tigrisdata.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TigrisStoreApplication {

  public static void main(String[] args) {
    new SpringApplication(TigrisStoreApplication.class).run(args);
  }
}

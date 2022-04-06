# tigrisdb-example-java
An example Java application demonstrating TigrisDB's usage.

[![java-ci](https://github.com/tigrisdata/tigrisdb-example-java/actions/workflows/java-ci.yml/badge.svg?branch=main)](https://github.com/tigrisdata/tigrisdb-example-java/actions/workflows/java-ci.yml)
# Key points

### Schema
TigrisDB schemas are located in `src/main/resources/tigrisdb-schema`.

Example schema

```json
{
  "name": "User",
  "description": "This document records the details of user for tigris store",
  "properties": {
    "id": {
      "description": "A unique identifier for the user",
      "type": "int"
    },
    "name": {
      "description": "Name of the user",
      "type": "string"
    },
    "balance": {
      "description": "user balance in USD",
      "type": "double"
    }
  },
  "primary_key": [
    "id"
  ]
}
```

### Schema to model generation

TigrisDB maven plugin reads these JSON schema files and generates Java models.
Refer in `pom.xml` for `com.tigrisdata.tools. code-generator:maven-plugin`
usage.

```xml

<plugin>
    <groupId>com.tigrisdata.tools.code-generator</groupId>
    <artifactId>maven-plugin</artifactId>
    <!-- we are still pre-release -->
    <version>1.0-SNAPSHOT</version>

    <executions>
        <execution>
            <goals>
                <goal>generate-sources</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <schemaDir>src/main/resources/tigrisdb-schema</schemaDir>
        <packageName>com.tigrisdata.store.generated</packageName>
        <outputDirectory>target/generated-sources</outputDirectory>
    </configuration>
</plugin>
```

### Operating on models

TigrisDB client API supports these generated models. You can use them to
represent your document.

```java
User alice = new User(1,"Alice");

TigrisCollection<User> userCollection=db.getCollection(User.class);
userCollection.insert(alice);
```

# Restful endpoints

Go to http://localhost:8080/swagger.html

![swagger_ui_screenshot.png](swagger_ui_screenshot.png)

# Note

We are still in pre-release and so `SNAPSHOT` artifacts are consumed.
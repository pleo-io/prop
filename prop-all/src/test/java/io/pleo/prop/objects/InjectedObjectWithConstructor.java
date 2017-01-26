package io.pleo.prop.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InjectedObjectWithConstructor {
  private String name;
  private int age;

  @JsonCreator
  public InjectedObjectWithConstructor(@JsonProperty("name") String name, @JsonProperty("age") int age) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}

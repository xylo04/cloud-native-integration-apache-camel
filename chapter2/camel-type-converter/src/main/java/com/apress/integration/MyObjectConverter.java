package com.apress.integration;

import io.quarkus.arc.Unremovable;
import javax.inject.Singleton;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;

@Singleton
@Unremovable
public class MyObjectConverter implements TypeConverters {

  @Converter
  public static AnotherObject toAnotherObject(MyObject object) {

    AnotherObject anotherObject = new AnotherObject();
    anotherObject.setValue(object.getValue());

    return anotherObject;
  }
}

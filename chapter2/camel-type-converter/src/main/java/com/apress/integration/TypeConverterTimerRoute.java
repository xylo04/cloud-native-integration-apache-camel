package com.apress.integration;

import java.util.UUID;
import org.apache.camel.builder.RouteBuilder;

@SuppressWarnings("unused")
public class TypeConverterTimerRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from("timer:type-converter-timer?period=2000")
        .routeId("type-converter-route")
        .process(
            exchange -> {
              MyObject object = new MyObject();
              object.setValue(UUID.randomUUID().toString());
              exchange.getMessage().setBody(object);
            })
        .convertBodyTo(AnotherObject.class)
        .log("${body}");
  }
}

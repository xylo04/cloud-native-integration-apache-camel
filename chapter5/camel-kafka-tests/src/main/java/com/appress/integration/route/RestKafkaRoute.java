package com.appress.integration.route;

import static com.appress.integration.constant.HTTPConstant.*;

import org.apache.camel.builder.RouteBuilder;

public class RestKafkaRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    rest("/kafka")
        .consumes(TEXT_PLAIN)
        .produces(TEXT_PLAIN)
        .post()
        .route()
        .routeId("rest-route")
        .log("sending message.")
        .removeHeaders("*")
        .to("{{kafka.uri.to}}")
        .removeHeaders("*")
        .setBody(constant("message sent."))
        .log("${body}")
        .endRest();
  }
}

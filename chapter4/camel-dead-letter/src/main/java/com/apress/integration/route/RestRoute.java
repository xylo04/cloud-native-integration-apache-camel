package com.apress.integration.route;

import static com.apress.integration.constant.HTTPConstant.*;

import org.apache.camel.builder.RouteBuilder;

public class RestRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    rest("/deadLetter")
        .consumes(TEXT_PLAIN)
        .produces(TEXT_PLAIN)
        .post()
        .route()
        .routeId("rest-route")
        .log("Redirecting message")
        .wireTap("seda:process-route")
        .setBody(constant("Thanks!"))
        .endRest();
  }
}

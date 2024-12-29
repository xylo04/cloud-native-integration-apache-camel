package com.apress.integration.health;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.camel.CamelContext;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;
import org.jboss.logging.Logger;

@Liveness
@Singleton
public class AppLiveness implements HealthCheck {

  private static final Logger LOG = Logger.getLogger(AppLiveness.class);

  @Inject CamelContext context;

  @Override
  public HealthCheckResponse call() {

    LOG.trace("Testing Liveness");

    HealthCheckResponseBuilder builder;

    if (!context.isStopped()) {
      builder = HealthCheckResponse.named("Camel UP").up();
    } else {
      builder = HealthCheckResponse.named("Camel DOWN").down();
    }

    return builder.build();
  }
}

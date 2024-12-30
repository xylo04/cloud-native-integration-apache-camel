package com.apress.integration.route;

import com.apress.integration.entity.Contact;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;

@SuppressWarnings("unused")
public class ContactListRoute extends RouteBuilder {

  public static final String MEDIA_TYPE_APP_JSON = "application/json";

  @Override
  public void configure() throws Exception {

    RestDefinition rest = rest("/contact").bindingMode(RestBindingMode.json);
    rest.post()
        .consumes(MEDIA_TYPE_APP_JSON)
        .produces(MEDIA_TYPE_APP_JSON)
        .type(Contact.class)
        .route()
        .routeId("save-contact-route")
        .log("saving contacts")
        .bean("contactsBean", "addContact")
        .endRest();
    rest.get()
        .produces(MEDIA_TYPE_APP_JSON)
        .route()
        .routeId("list-contact-route")
        .log("listing contacts")
        .bean("contactsBean", "listContacts")
        .endRest();
  }
}

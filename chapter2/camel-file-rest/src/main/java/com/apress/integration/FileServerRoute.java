package com.apress.integration;

import java.net.URISyntaxException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestDefinition;

@SuppressWarnings("unused")
public class FileServerRoute extends RouteBuilder {

  public static final String MEDIA_TYPE_APP_JSON = "application/json";
  public static final String DIRECT_SAVE_FILE = "direct:save-file";
  public static final String DIRECT_GET_FILES = "direct:get-files";
  public static final String CODE_500_MESSAGE = "Failure on the server side.";
  public static final String CODE_204_MESSAGE = "No files found on the server.";

  private static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain";
  private static final String CODE_201_MESSAGE = "File created on the server.";

  @Override
  public void configure() throws Exception {
    createFileServerApiDefinition();
    createFileServerRoutes();
  }

  private void createFileServerApiDefinition() {
    restConfiguration()
        .apiContextPath("/fileServer/doc")
        .apiProperty("api.title", "File Server API")
        .apiProperty("api.version", "1.0.0")
        .apiProperty("api.description", "REST API to save files");

    RestDefinition rest = rest("/fileServer");
    rest.get("/file")
        .id("get-files")
        .description("Generates a list of saved files")
        .produces(MEDIA_TYPE_APP_JSON)
        .responseMessage()
        .code(200)
        .endResponseMessage()
        .responseMessage()
        .code(204)
        .message(CODE_204_MESSAGE)
        .endResponseMessage()
        .responseMessage()
        .code(500)
        .message(CODE_500_MESSAGE)
        .endResponseMessage()
        .to(DIRECT_GET_FILES);
    rest.post("/file")
        .id("save-file")
        .description(
            "Saves the HTTP Request body into a File, using the fileName header to set the file"
                + " name. ")
        .consumes(MEDIA_TYPE_TEXT_PLAIN)
        .produces(MEDIA_TYPE_TEXT_PLAIN)
        .responseMessage()
        .code(201)
        .message(CODE_201_MESSAGE)
        .endResponseMessage()
        .responseMessage()
        .code(500)
        .message(CODE_500_MESSAGE)
        .endResponseMessage()
        .to(DIRECT_SAVE_FILE);
  }

  private void createFileServerRoutes() throws URISyntaxException {
    createSaveFileRoute();
    createGetFilesRoute();
  }

  private void createSaveFileRoute() throws URISyntaxException {
    from(DIRECT_SAVE_FILE)
        .routeId("save-file")
        .setHeader(Exchange.FILE_NAME, simple("${header.fileName}"))
        .to("file:" + FileReaderBean.getServerDirURI())
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        .setHeader(Exchange.CONTENT_TYPE, constant(MEDIA_TYPE_TEXT_PLAIN))
        .setBody(constant(CODE_201_MESSAGE));
  }

  private void createGetFilesRoute() {
    from(DIRECT_GET_FILES)
        .routeId("get-files")
        .log("getting files list")
        .bean(FileReaderBean.class, "listFile")
        .choice()
        .when(simple("${body.size} > 0"))
        .marshal()
        .json(JsonLibrary.Jsonb)
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        .endChoice()
        .otherwise()
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404));
  }
}

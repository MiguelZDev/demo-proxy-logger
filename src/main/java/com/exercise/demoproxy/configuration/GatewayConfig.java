package com.exercise.demoproxy.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GatewayConfig {

  @Bean
  public RouteLocator myRouteSavingRequestBody(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("list-metadata",
        p -> p.path("/get")
          .filters(f -> f
            .modifyRequestBody(String.class, String.class,
              (webExchange, originalBody) -> {
                if (originalBody != null) {
                  log.info("[lean-tech] Request body \n{}", originalBody);
                  return Mono.just(originalBody);
                } else {
                  return Mono.empty();
                }
              })
            .modifyResponseBody(String.class, String.class,
              (webExchange, originalBody) -> {
                if (originalBody != null) {
                  log.info("[lean-tech] Response body \n{}", originalBody);
                  return Mono.just(originalBody);
                } else {
                  return Mono.empty();
                }
              })
          )
          .uri("http://httpbin.org:80")
      )
      .build();
  }


}

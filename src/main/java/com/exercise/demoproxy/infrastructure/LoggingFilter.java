package com.exercise.demoproxy.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestMethod = exchange.getRequest().getMethod().toString();
    String headers = exchange.getRequest().getHeaders().toSingleValueMap().toString();
    log.info("[lean-tech] Request method {} - headers: {}", requestMethod, headers);

    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
      String responseHeaders = exchange.getRequest().getHeaders().toSingleValueMap().toString();
      int status = exchange.getResponse().getStatusCode().value();
      log.info("[lean-tech] Response status {} - headers: {}", status, responseHeaders);
    }));
  }

}

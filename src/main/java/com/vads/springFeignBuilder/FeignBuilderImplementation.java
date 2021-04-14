package com.vads.springFeignBuilder;

import com.vads.springFeignBuilder.configuration.FeignBasicConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = {FeignBasicConfiguration.class})
public class FeignBuilderImplementation {

  public static void main(String[] args) {
    SpringApplication.run(FeignBuilderImplementation.class, args);
  }
}

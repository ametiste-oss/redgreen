package org.ametiste.redgreen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ApplicationContext;

/**
 *
 * @since
 */
@SpringBootApplication
@EnableCircuitBreaker
public class RedgreenApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RedgreenApplication.class, args);
    }

}

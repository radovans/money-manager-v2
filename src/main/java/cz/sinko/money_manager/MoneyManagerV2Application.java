package cz.sinko.money_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;


/**
 * Main class for running the application.
 *
 * @author Sinko Radovan
 */
@Slf4j
@SpringBootApplication
public class MoneyManagerV2Application {

    public static void main(String[] args) {
        SpringApplication.run(MoneyManagerV2Application.class, args);
        log.info("API health check: http://localhost:8080/actuator/health");
    }
}

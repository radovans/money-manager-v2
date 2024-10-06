package cz.sinko.moneymanager.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Controller for processing csv bank statements.
 *
 * @author Sinko Radovan
 */
@AllArgsConstructor
@RestController
@RequestMapping("/process")
@Slf4j
public class ProcessController {

    /**
     * Hello world endpoint.
     *
     * @return void
     */
    @GetMapping(value = "/hello")
    public ResponseEntity<Void> helloWorld() {
        log.info("Hello World");
        return ResponseEntity.ok().build();
    }
}

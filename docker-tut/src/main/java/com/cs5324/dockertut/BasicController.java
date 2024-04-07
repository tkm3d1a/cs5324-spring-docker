package com.cs5324.dockertut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/basic")
public class BasicController {

    @GetMapping("/isAvailable")
    public ResponseEntity<?> isAvailable(){
        BasicResponseDTO resp = new BasicResponseDTO("available");
        log.info("isAvailable CALLED");
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping
    public ResponseEntity<?> home(){
        BasicResponseDTO resp = new BasicResponseDTO("You have hit the home endpoint of the docker-tut program");
        log.info("/ CALLED");
        return ResponseEntity.ok().body(resp);
    }
}

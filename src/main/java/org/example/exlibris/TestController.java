package org.example.exlibris;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<?> sendOk() {
        String ok = "OK";
        return ResponseEntity.ok(ok);
    }
}

package com.coldie.springsecurity.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@Tag(name = "Demo")
//@SecurityRequirement(name = "bearerAuth")
public class DemoController {

    @Operation(
            description = "Just an endpoint",
            summary = "Chill it's just an endpoint.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<String>("Hmmm!", HttpStatus.OK);
    }
}

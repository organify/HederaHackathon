package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.TransferRequestModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TransferController {
    @PostMapping("/api/transfer")
    public ResponseEntity<String> Post(@RequestBody TransferRequestModel request) {

        return ResponseEntity.ok("Request accepted, money sent");
    }
}

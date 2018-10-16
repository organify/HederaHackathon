package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.TransferRequestModel;
import com.example.hederaHashgraphApi.Model.TransferResponseObject;
import com.example.hederaHashgraphApi.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/api/transfer")
    public ResponseEntity<TransferResponseObject> Post(@RequestBody TransferRequestModel request) {

        try{
            var balance = accountService.send(request.data);
            return balance > 0? ResponseEntity.ok(new TransferResponseObject(balance)):
                     ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

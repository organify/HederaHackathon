package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.RequestDataModel;
import com.example.hederaHashgraphApi.Model.TransferRequestModel;
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
    public ResponseEntity<String> Post(@RequestBody RequestDataModel<TransferRequestModel> request) {

        return accountService.send(request.data)
                ? ResponseEntity.ok("Request accepted, money sent")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

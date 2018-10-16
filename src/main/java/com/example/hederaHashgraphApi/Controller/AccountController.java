package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.CreateRequestModel;
import com.example.hederaHashgraphApi.Model.RequestDataModel;
import com.example.hederaHashgraphApi.Service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.spec.InvalidKeySpecException;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/api/createAccount")
    public JSONObject createAccount(@RequestBody RequestDataModel<CreateRequestModel> request) throws InvalidKeySpecException, Exception {

        // create the new account
        accountService.createNewAccount(request.data.accountId);

        var data = "Account has been created";
        JSONObject result = new JSONObject();
        result.put("data", data);
        return result;
    }
}


package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.AppProperties;
import com.example.hederaHashgraphApi.Service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.spec.InvalidKeySpecException;

@RestController
@EnableConfigurationProperties(AppProperties.class)
public class OrganifyController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/api/createAccount", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject createAccount() throws InvalidKeySpecException, Exception {

        // create the new account
        accountService.createNewAccount();

        var data = "Account has been created";
        JSONObject result = new JSONObject();
        result.put("data", data);
        return result;
    }

    @RequestMapping(value = "/api/transfer", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject transfer(@RequestBody JSONObject input) {



        var data = "Tokens have been transferred";
        JSONObject result = new JSONObject();
        result.put("data", data);
        return result;
    }
}


package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.AccountCreate;
import com.example.hederaHashgraphApi.Model.AppProperties;
import com.example.hederaHashgraphApi.Utilities.ConfigService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.spec.InvalidKeySpecException;

@RequestMapping("/api/greeting")
@RestController
@EnableConfigurationProperties(AppProperties.class)
public class GreetingController {
    @Autowired
    private AccountCreate account;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject index() throws InvalidKeySpecException, Exception {
        account.CreateNewAccount();
        var data = "Hello";
        JSONObject obj = new JSONObject();

        obj.put("data", data);
        return obj;
    }
}

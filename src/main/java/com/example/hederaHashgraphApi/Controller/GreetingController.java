package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Service.ConfigService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.spec.InvalidKeySpecException;

@RestController
public class GreetingController {
    @Autowired
    private ConfigService config;
    @GetMapping("/api/greeting")
    public JSONObject index() throws InvalidKeySpecException {
        //var t = config.getTxQueryDefaults();
        var data = "Hello";
        JSONObject obj = new JSONObject();

        obj.put("data", data);
        return obj;
    }
}

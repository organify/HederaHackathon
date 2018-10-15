package com.example.hederaHashgraphApi.Controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class TransferController {
    @PostMapping("api/transfer")
    public JSONObject Post() {
        //var t = config.getTxQueryDefaults();
        var data = "Hello";
        JSONObject obj = new JSONObject();

        obj.put("data", data);
        return obj;
    }
}

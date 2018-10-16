package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Service.KeystoreService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeystoreController {

    @Autowired
    private KeystoreService keystoreService;

    @PostMapping("/api/initKeystore")
    public ResponseEntity<String> initKeystore(@RequestBody JSONObject request) {

        return keystoreService.createFile(request)
                ? ResponseEntity.ok("Keystore file has been created")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Service.FileService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * Initially stores the earlier created keystore data into the file system.
     *
     * @param request
     * @return
     */
    @PostMapping("/api/initKeystore")
    public ResponseEntity<String> initKeystore(@RequestBody JSONObject request) {

        return fileService.createFile(request)
                ? ResponseEntity.ok("Keystore file has been created")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

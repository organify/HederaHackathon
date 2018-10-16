package com.example.hederaHashgraphApi.Service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeystoreService {

    final Logger logger = LoggerFactory.getLogger(KeystoreService.class);

    /**
     * Creates an initial keystore file on the ledger. (Importing earlier created accounts)
     *
     * @param data
     * @return
     */
    public Boolean createFile (JSONObject data) {

        byte[] bytes = data.toJSONString().getBytes();

        logger.info(data.toJSONString());
        return true;
    }
}

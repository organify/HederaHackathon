package com.example.hederaHashgraphApi.Service;

import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class AccountService extends AbstractBaseService {

    public Boolean send() {
        try {
            this.getTxQueryDefaults();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

}

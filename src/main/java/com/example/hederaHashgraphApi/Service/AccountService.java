package com.example.hederaHashgraphApi.Service;

import java.security.spec.InvalidKeySpecException;

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

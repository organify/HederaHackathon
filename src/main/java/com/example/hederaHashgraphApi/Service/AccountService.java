package com.example.hederaHashgraphApi.Service;

import com.example.hederaHashgraphApi.Model.TransferRequestDataModel;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class AccountService extends AbstractBaseService {

    public Boolean send(TransferRequestDataModel data) {
        try {
            this.getTxQueryDefaults();

            // TODO Implement Hedera SDK API send call here
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

}

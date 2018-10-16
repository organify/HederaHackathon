package com.example.hederaHashgraphApi.Controller;

import com.example.hederaHashgraphApi.Model.BalanceResponseObject;
import com.example.hederaHashgraphApi.Model.BaseRequestModel;
import com.example.hederaHashgraphApi.Model.RequestDataModel;
import com.example.hederaHashgraphApi.Model.TransferRequestModel;
import com.example.hederaHashgraphApi.Service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.spec.InvalidKeySpecException;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Called from the REST API to createSmartContract a new Hedera Account.
     *
     * @param request contains a JSON structure providing the master account number to
     *                createSmartContract the account for.
     * @return
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    @PostMapping(value = "/api/createAccount")
    public JSONObject createAccount(@RequestBody RequestDataModel<BaseRequestModel> request) throws InvalidKeySpecException, Exception {

        // createSmartContract the new account
        long accountNum = accountService.createNewAccount(request.data.accountId);

        var data = "Account with number " + accountNum + " has been created.";
        JSONObject result = new JSONObject();
        result.put("data", data);
        return result;
    }

    /**
     * Called from the REST API to transfer tokens.
     *
     * @param request conains a JSON structure with the involved account and the
     *                tokens to be transferred.
     * @return
     */
    @PostMapping("/api/transfer")
    public ResponseEntity<String> transfer(@RequestBody RequestDataModel<TransferRequestModel> request) {

        try{
            return accountService.send(request.data)? ResponseEntity.ok("Success"):
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Gets the balance for a certain account.
     *
     * @param accountId the id of the account.
     *
     * @return
     */
    @GetMapping("/api/getBalance")
    public ResponseEntity<BalanceResponseObject> getBalance(String accountId) {

        try{
            var balance = accountService.getAccountBalance(accountId);
            return ResponseEntity.ok(new BalanceResponseObject(balance));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

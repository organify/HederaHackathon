package com.example.hederaHashgraphApi.Model;

/**
 * Balance object returned by the rest API.
 */
public class BalanceResponseObject {

    public long accountBalance;

    /**
     * Creates a new balance result object returned by the REST API.
     *
     * @param balance the balance of the account
     */
    public BalanceResponseObject(long balance){
        accountBalance = balance;
    }
}

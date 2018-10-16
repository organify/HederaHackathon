package com.example.hederaHashgraphApi.Model;

public class TransferResponseObject {
    public long toAccountBalance;
    public TransferResponseObject(long balance){
        toAccountBalance = balance;
    }
}

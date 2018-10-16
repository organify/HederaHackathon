package com.example.hederaHashgraphApi.Model;

/**
 * Data model of the request input to transfer Tokens between two Hedera accounts.
 */
public class TransferRequestModel {
    public String to;
    public String from;
    public long amount;
}

package com.example.hederaHashgraphApi.Model;

public class AppProperties {
    private String nodeAddress;
    private int nodePort;
    private long nodeAccountShard;
    private long nodeAccountRealm;
    private long nodeAccountNum;
    private String publicKey;
    private String privateKey;
    private long payAccountShard;
    private long payAccountRealm;
    private long payAccountNum;

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public long getNodeAccountShard() {
        return nodeAccountShard;
    }

    public void setNodeAccountShard(long nodeAccountShard) {
        this.nodeAccountShard = nodeAccountShard;
    }

    public long getNodeAccountRealm() {
        return nodeAccountRealm;
    }

    public void setNodeAccountRealm(long nodeAccountRealm) {
        this.nodeAccountRealm = nodeAccountRealm;
    }

    public long getNodeAccountNum() {
        return nodeAccountNum;
    }

    public void setNodeAccountNum(long nodeAccountNum) {
        this.nodeAccountNum = nodeAccountNum;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public long getPayAccountShard() {
        return payAccountShard;
    }

    public void setPayAccountShard(long payAccountShard) {
        this.payAccountShard = payAccountShard;
    }

    public long getPayAccountRealm() {
        return payAccountRealm;
    }

    public void setPayAccountRealm(long payAccountRealm) {
        this.payAccountRealm = payAccountRealm;
    }

    public long getPayAccountNum() {
        return payAccountNum;
    }

    public void setPayAccountNum(long payAccountNum) {
        this.payAccountNum = payAccountNum;
    }
}

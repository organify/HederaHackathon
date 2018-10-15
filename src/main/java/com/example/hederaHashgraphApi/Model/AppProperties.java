package com.example.hederaHashgraphApi.Model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Java Developer Zone on 26-08-2017.
 */
@ConfigurationProperties()
public class AppProperties {

    public String nodeAddress = "";
    public int nodePort = 0;

    public long nodeAccountShard = 0;
    public long nodeAccountRealm = 0;
    public long nodeAccountNum = 0;

    public String publicKey = System.getProperty("publicKey","");
    public String privateKey = System.getProperty("privateKey","");

    public long accountShard = 0;
    public long accountRealm = 0;
    public long accountNum = 0;
}

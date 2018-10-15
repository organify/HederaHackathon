package com.example.hederaHashgraphApi.Service;

import com.example.hederaHashgraphApi.Model.AppProperties;
import com.hedera.sdk.common.HederaAccountID;
import com.hedera.sdk.common.HederaDuration;
import com.hedera.sdk.common.HederaKey;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.node.HederaNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public abstract class AbstractBaseService {

    @Autowired
    private Environment environment;

    private AppProperties getAppProperties() {
        AppProperties appProperties = new AppProperties();

        appProperties.setNodeAddress(environment.getProperty("nodeaddress"));
        appProperties.setNodePort(Integer.parseInt(environment.getProperty("nodeport")));
        appProperties.setNodeAccountShard(Long.parseLong(environment.getProperty("nodeAccountShard")));
        appProperties.setNodeAccountRealm(Long.parseLong(environment.getProperty("nodeAccountRealm")));
        appProperties.setNodeAccountNum(Long.parseLong(environment.getProperty("nodeAccountNum")));

        appProperties.setPublicKey(environment.getProperty("pubkey"));
        appProperties.setPrivateKey(environment.getProperty("privkey"));

        appProperties.setPayAccountShard(Long.parseLong(environment.getProperty("payingAccountShard")));
        appProperties.setPayAccountRealm(Long.parseLong(environment.getProperty("payingAccountRealm")));
        appProperties.setPayAccountNum(Long.parseLong(environment.getProperty("payingAccountNum")));

        return appProperties;
    }

    protected HederaTransactionAndQueryDefaults getTxQueryDefaults() throws InvalidKeySpecException {
        AppProperties appProperties = getAppProperties();

        // setup node account ID
        HederaAccountID nodeAccountID = new HederaAccountID(appProperties.getNodeAccountShard(), appProperties.getNodeAccountRealm(), appProperties.getNodePort());

        // setup node
        HederaNode node = new HederaNode(appProperties.getNodeAddress(), appProperties.getNodePort(), nodeAccountID);

        // setup paying account
        HederaAccountID payingAccountID = new HederaAccountID(appProperties.getPayAccountShard(), appProperties.getPayAccountRealm(), appProperties.getPayAccountNum());

        // setup paying keypair
        HederaCryptoKeyPair payingKeyPair = new HederaCryptoKeyPair(HederaKey.KeyType.ED25519, appProperties.getPublicKey(), appProperties.getPrivateKey());

        // setup a set of defaults for query and transactions
        HederaTransactionAndQueryDefaults txQueryDefaults = new HederaTransactionAndQueryDefaults();

        txQueryDefaults.memo = "Demo memo";
        txQueryDefaults.node = node;
        txQueryDefaults.payingAccountID = payingAccountID;
        txQueryDefaults.payingKeyPair = payingKeyPair;
        txQueryDefaults.transactionValidDuration = new HederaDuration(120, 0);

        return txQueryDefaults;
    }

}

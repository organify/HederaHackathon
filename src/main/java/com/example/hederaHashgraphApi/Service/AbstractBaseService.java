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

        appProperties.setNodeAddress(environment.getProperty("node.address"));
        appProperties.setNodePort(Integer.parseInt(environment.getProperty("node.port")));
        appProperties.setNodeAccountShard(Long.parseLong(environment.getProperty("node.account.shard")));
        appProperties.setNodeAccountRealm(Long.parseLong(environment.getProperty("node.account.realm")));
        appProperties.setNodeAccountNum(Long.parseLong(environment.getProperty("node.account.num")));

        appProperties.setPayAccountShard(Long.parseLong(environment.getProperty("paying.account.shard")));
        appProperties.setPayAccountRealm(Long.parseLong(environment.getProperty("paying.account.realm")));

        return appProperties;
    }

    private void setAccountProperties(String accountID, AppProperties appProperties) {
        appProperties.setPublicKey(environment.getProperty(String.format("%s.public.key", accountID)));
        appProperties.setPrivateKey(environment.getProperty(String.format("%s.private.key", accountID)));
        appProperties.setPayAccountNum(Long.parseLong(accountID));
    }

    protected HederaTransactionAndQueryDefaults getTxQueryDefaults(String accountID) throws InvalidKeySpecException {
        AppProperties appProperties = getAppProperties();
        setAccountProperties(accountID, appProperties);

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

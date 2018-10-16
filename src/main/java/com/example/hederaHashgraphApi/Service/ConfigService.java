package com.example.hederaHashgraphApi.Service;

import java.security.spec.InvalidKeySpecException;
import com.hedera.sdk.common.HederaAccountID;
import com.hedera.sdk.common.HederaDuration;
import com.hedera.sdk.common.HederaKey.KeyType;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.node.HederaNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class ConfigService {

	public String nodeAddress = "";
	public int nodePort = 0;

	public long nodeAccountShard = 0;
	public long nodeAccountRealm = 0;
	public long nodeAccountNum = 0;

	public String publicKey = "";
	public String privateKey = "";

	public long accountShard = 0;
	public long accountRealm = 0;
	public long accountNum = 0;


	@Autowired
	private Environment applicationProperties;
	public void getNodeDetails() {

		try {

			// get the property value and print it out
			nodeAddress = applicationProperties.getProperty("nodeAddress");
			nodePort = Integer.parseInt(applicationProperties.getProperty("nodePort"));

			nodeAccountShard = Long.parseLong(applicationProperties.getProperty("nodeAccountShard"));
			nodeAccountRealm = Long.parseLong(applicationProperties.getProperty("nodeAccountRealm"));
			nodeAccountNum = Long.parseLong(applicationProperties.getProperty("nodeAccountNum"));

			publicKey = applicationProperties.getProperty("publicKey");
			privateKey = applicationProperties.getProperty("privateKey");

			accountShard = Long.parseLong(applicationProperties.getProperty("accountShard"));
			accountRealm = Long.parseLong(applicationProperties.getProperty("accountRealm"));
			accountNum = Long.parseLong(applicationProperties.getProperty("accountNum"));

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public HederaTransactionAndQueryDefaults getTxQueryDefaults() throws InvalidKeySpecException {
		// Get node details 
		getNodeDetails();

		// setup node account ID
		HederaAccountID nodeAccountID = new HederaAccountID(nodeAccountShard, nodeAccountRealm, nodeAccountNum);
		// setup node
		HederaNode node = new HederaNode(nodeAddress, nodePort, nodeAccountID);

		// setup paying account
		HederaAccountID accountID = new HederaAccountID(accountShard, accountRealm, accountNum);

		// setup paying keypair
		HederaCryptoKeyPair keyPair = new HederaCryptoKeyPair(KeyType.ED25519, publicKey, privateKey);

		// setup a set of defaults for query and transactions
		HederaTransactionAndQueryDefaults txQueryDefaults = new HederaTransactionAndQueryDefaults();

		txQueryDefaults.memo = "Default memo";
		txQueryDefaults.node = node;
		txQueryDefaults.payingAccountID = accountID;
		txQueryDefaults.payingKeyPair = keyPair;
		txQueryDefaults.transactionValidDuration = new HederaDuration(120, 0);

		return txQueryDefaults;
	}

}

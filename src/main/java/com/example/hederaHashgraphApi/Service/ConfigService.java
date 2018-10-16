package com.example.hederaHashgraphApi.Service;

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

/**
 * Service to take care about reading and setting up the Hedera Account/Tx configuration.
 */
@Service
public class ConfigService {
	public String nodeAddress = "";
	public int nodePort = 0;

	public long nodeAccountShard = 0;
	public long nodeAccountRealm = 0;
	public long nodeAccountNum = 0;

	public String pubKey = "";
	public String privKey = "";

	public long payAccountShard = 0;
	public long payAccountRealm = 0;
	public long payAccountNum = 0;
	@Autowired
	private Environment applicationProperties;
	public void getNodeDetails() {

		try {

			// get the property value and print it out
			nodeAddress = applicationProperties.getProperty("nodeaddress");
			nodePort = Integer.parseInt(applicationProperties.getProperty("nodeport"));

			nodeAccountShard = Long.parseLong(applicationProperties.getProperty("nodeAccountShard"));
			nodeAccountRealm = Long.parseLong(applicationProperties.getProperty("nodeAccountRealm"));
			nodeAccountNum = Long.parseLong(applicationProperties.getProperty("nodeAccountNum"));

			pubKey = applicationProperties.getProperty("pubkey");
			privKey = applicationProperties.getProperty("privkey");

			payAccountShard = Long.parseLong(applicationProperties.getProperty("payingAccountShard"));
			payAccountRealm = Long.parseLong(applicationProperties.getProperty("payingAccountRealm"));
			payAccountNum = Long.parseLong(applicationProperties.getProperty("payingAccountNum"));

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
		HederaAccountID payingAccountID = new HederaAccountID(payAccountShard, payAccountRealm, payAccountNum);

		// setup paying keypair
		HederaCryptoKeyPair payingKeyPair = new HederaCryptoKeyPair(KeyType.ED25519, pubKey, privKey);

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

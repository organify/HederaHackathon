package com.example.hederaHashgraphApi.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.common.HederaPrecheckResult;
import com.hedera.sdk.common.HederaTransactionReceipt;
import com.hedera.sdk.common.HederaTransactionStatus;
import com.hedera.sdk.common.Utilities;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.transaction.HederaTransactionResult;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class AccountService {

	@Autowired
	private ConfigService config;

	final Logger logger = LoggerFactory.getLogger(AccountService.class);

	/**
	 * Creates a new Hedera account.
	 *
	 * @param account the Account to be created
	 * @param keyPair the public and private key pair
	 * @param initialBalance initial balance of the account
	 * @return
	 * @throws Exception
	 */
	private HederaAccount createAccount(HederaAccount account,
										HederaCryptoKeyPair keyPair,
										long initialBalance) throws Exception {

		// new account properties
		long shardNum = 0;
		long realmNum = 0;

		logger.info("Creating crypto account...");

		// createAccount the new account
		// account creation transaction
		HederaTransactionResult createResult = account.create(shardNum, realmNum,
				keyPair.getPublicKey(), keyPair.getKeyType(),
				initialBalance, null);

		// was it successful ?
		if (createResult.getPrecheckResult() == HederaPrecheckResult.OK) {
			// yes, get a receipt for the transaction
			HederaTransactionReceipt receipt = Utilities.getReceipt(account.hederaTransactionID, account.txQueryDefaults.node);
			// was that successful ?
			if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
				// yes, get the new account number from the receipt
				account.accountNum = receipt.accountID.accountNum;
				// and print it out
				logger.info(String.format("Your new account number is %d", account.accountNum));
			} else {
				logger.info("transactionStatus not SUCCESS: " + receipt.transactionStatus.name());
				return null;
			}
		} else {
			logger.info("getPrecheckResult not OK: " + createResult.getPrecheckResult().name());
			return null;
		}
		return account;
	}

	/**
	 * Called by the controller to create a new account.
	 *
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public void createNewAccount() throws InvalidKeySpecException, Exception  {

		// new account object
		HederaAccount account = new HederaAccount();

		// setup transaction/query defaults configuration
		HederaTransactionAndQueryDefaults txQueryDefaults = config.getTxQueryDefaults();
		txQueryDefaults.generateRecord = true;
		account.txQueryDefaults = txQueryDefaults;

		// create an account
		account = this.createAccount(account, txQueryDefaults.payingKeyPair, 100000);
		if (account != null) {
			logger.info("Account has been created successfully");
		} else {
			logger.info("Account creation has failed");
			throw new Exception("Account creation has failed");
		}

	}


}

package com.example.hederaHashgraphApi;

import com.example.hederaHashgraphApi.Utilities.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.common.HederaPrecheckResult;
import com.hedera.sdk.common.HederaTransactionReceipt;
import com.hedera.sdk.common.HederaTransactionStatus;
import com.hedera.sdk.common.Utilities;
import com.hedera.sdk.common.HederaKey.KeyType;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.transaction.HederaTransactionResult;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class AccountCreate {
	@Autowired
	private ConfigService config;
	final Logger logger = LoggerFactory.getLogger(AccountCreate.class);
	private HederaAccount create(HederaAccount account, HederaCryptoKeyPair newAccountKey, long initialBalance) throws Exception {

		// new account properties
		long shardNum = 0;
		long realmNum = 0;
		
		logger.info("");
		logger.info("CRYPTO CREATE ACCOUNT");
		logger.info("");

		// create the new account
		// account creation transaction
		HederaTransactionResult createResult = account.create(shardNum, realmNum, newAccountKey.getPublicKey(), newAccountKey.getKeyType(), initialBalance, null);
		// was it successful ?
		if (createResult.getPrecheckResult() == HederaPrecheckResult.OK) {
			// yes, get a receipt for the transaction
			HederaTransactionReceipt receipt = Utilities.getReceipt(account.hederaTransactionID, account.txQueryDefaults.node);
			// was that successful ?
			if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
				// yes, get the new account number from the receipt
				account.accountNum = receipt.accountID.accountNum;
				// and print it out
				logger.info(String.format("===>Your new account number is %d", account.accountNum));
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
	public void CreateNewAccount() throws InvalidKeySpecException, Exception  {
		//DO NOT CHANGE THESE, CHANGE BELOW INSTEAD
		boolean create = false; //OK
		boolean balance = false; //OK
		boolean send = false; //NOK
		boolean info = false; //OK
		boolean update = false; //OK
		boolean doAddClaim = false;//OK
		boolean getTXRecord = false;

		// setup a set of defaults for query and transactions
		HederaTransactionAndQueryDefaults txQueryDefaults = new HederaTransactionAndQueryDefaults();
		txQueryDefaults = config.getTxQueryDefaults();

		// new account objects
		HederaAccount account = new HederaAccount();
		HederaAccount accountXferTo = new HederaAccount();

		// setup transaction/query defaults (durations, etc...)
		account.txQueryDefaults = txQueryDefaults;
		accountXferTo.txQueryDefaults = txQueryDefaults;

		create = true;
		balance = true;
		send = true;
		info = true;
		update = true;
//    	doAddClaim = true; -- not implemented ?
		getTXRecord = true;

		// create an account
		if (create) {
			account.txQueryDefaults.generateRecord = getTXRecord;
			//HederaCryptoKeyPair newAccountKey = new HederaCryptoKeyPair(KeyType.ED25519);
			//HederaCryptoKeyPair accountXferToKey = new HederaCryptoKeyPair(KeyType.ED25519);

			account = this.create(account, txQueryDefaults.payingKeyPair, 100000);
			//create(account, newAccountKey,100000);
			if (account == null) {
				logger.info("*******************************************");
				logger.info("FIRST ACCOUNT CREATE FAILED");
				logger.info("*******************************************");
				throw new Exception("Account create failure");
			} else {
				if (getTXRecord) {
					//HederaTransactionID txID = account.hederaTransactionID;
					//HederaTransactionRecord txRecord = new HederaTransactionRecord(txID, 10, txQueryDefaults);
				}
			}
			//account.txQueryDefaults.generateRecord = false;

		}
	}
}

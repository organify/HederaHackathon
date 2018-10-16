package com.example.hederaHashgraphApi.Service;

import com.example.hederaHashgraphApi.Model.TransferRequestDataModel;
import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.common.*;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.transaction.HederaTransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.spec.InvalidKeySpecException;

@Service
public class AccountService extends AbstractBaseService {
    final Logger logger = LoggerFactory.getLogger(AccountService.class);

    /**
     * Called by the controller to create a new account.
     *
     * @param accountId the master accountId of the User creating the Hedera Account.
     *
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    public void createNewAccount(String accountId) throws InvalidKeySpecException, Exception  {

        // new account object
        HederaAccount account = new HederaAccount();

        // setup transaction/query defaults configuration
        HederaTransactionAndQueryDefaults txQueryDefaults = getTxQueryDefaults(accountId);
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

    /**
     * Internal method to creates a new account.
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
            HederaTransactionReceipt receipt = Utilities.getReceipt(
                    account.hederaTransactionID,
                    account.txQueryDefaults.node,
                    100, 50, 50);
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
     * Transfers tokens between two account according to the data received
     * through the REST API.
     *
     * @param data request data received through the REST API
     * @return true if the transaction has been processed successfully.
     */
    public Boolean send(TransferRequestDataModel data) {
        try {
            HederaTransactionAndQueryDefaults txQueryDefaultsTo = this.getTxQueryDefaults(data.to);
            HederaTransactionAndQueryDefaults txQueryDefaultsFrom = this.getTxQueryDefaults(data.from);

            // new account objects
            HederaAccount accountFrom = new HederaAccount(new HederaTransactionID(txQueryDefaultsFrom.payingAccountID));
            HederaAccount accountTo = new HederaAccount(new HederaTransactionID(txQueryDefaultsTo.payingAccountID));

            // setup transaction/query defaults (durations, etc...)
            accountFrom.txQueryDefaults = txQueryDefaultsFrom;
            accountFrom.setNode(txQueryDefaultsFrom.node);
            accountTo.txQueryDefaults = txQueryDefaultsTo;
            accountTo.setNode(txQueryDefaultsTo.node);

            // make the transfer
            HederaTransactionResult transferResult = accountFrom.send(accountTo.getHederaAccountID(), data.amount);

            // was it successful ?
            if (transferResult.getPrecheckResult() == HederaPrecheckResult.OK) {
                // yes, get a receipt for the transaction
                HederaTransactionReceipt receipt  = Utilities.getReceipt(
                        accountFrom.hederaTransactionID,
                        accountFrom.txQueryDefaults.node,
                        100, 50, 50);
                if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
                    // if query successful, print it
                    logger.info("Transfer has been successful");
                    return true;
                } else {
                    logger.info("Transfer failed with transactionStatus:" + receipt.transactionStatus.toString());
                }
            } else {
                logger.info("Transfer failed with getPrecheckResult:" + transferResult.getPrecheckResult().toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}

package com.example.hederaHashgraphApi.Service;

import com.example.hederaHashgraphApi.Model.TransferRequestDataModel;
import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.common.*;
import com.hedera.sdk.transaction.HederaTransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends AbstractBaseService {
    final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public Boolean send(TransferRequestDataModel data) {
        try {
            HederaTransactionAndQueryDefaults txQueryDefaultsTo = this.getTxQueryDefaults(data.to);
            HederaTransactionAndQueryDefaults txQueryDefaultsFrom = this.getTxQueryDefaults(data.from);

            // new account objects
            HederaAccount account = new HederaAccount(new HederaTransactionID(txQueryDefaultsFrom.payingAccountID));
            HederaAccount accountXferTo = new HederaAccount(new HederaTransactionID(txQueryDefaultsTo.payingAccountID));

            account.txQueryDefaults.generateRecord = true;
            // setup transaction/query defaults (durations, etc...)
            account.txQueryDefaults = txQueryDefaultsFrom;
            account.setNode(txQueryDefaultsFrom.node);
            accountXferTo.txQueryDefaults = txQueryDefaultsTo;
            accountXferTo.setNode(txQueryDefaultsTo.node);
            // make the transfer
            HederaTransactionResult transferResult = account.send(accountXferTo.getHederaAccountID(), data.amount);

            // was it successful ?
            if (transferResult.getPrecheckResult() == HederaPrecheckResult.OK) {
                // yes, get a receipt for the transaction
                HederaTransactionReceipt receipt  = Utilities.getReceipt(account.hederaTransactionID,  account.txQueryDefaults.node);
                if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
                    // if query successful, print it
                    logger.info("===>Transfer successful");
                } else {
                    logger.info("Failed with transactionStatus:" + receipt.transactionStatus.toString());
                }
            } else {
                logger.info("Failed with getPrecheckResult:" + transferResult.getPrecheckResult().toString());
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}

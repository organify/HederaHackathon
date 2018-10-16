package com.example.hederaHashgraphApi.Service;

import com.hedera.sdk.common.*;
import com.hedera.sdk.file.HederaFile;
import com.hedera.sdk.file.HederaFileCreateDefaults;
import com.hedera.sdk.transaction.HederaTransactionResult;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KeystoreService extends AbstractBaseService {

    final Logger logger = LoggerFactory.getLogger(KeystoreService.class);

    /**
     * Creates an initial keystore file on the ledger. (Importing earlier created accounts)
     *
     * @param data the JSON structure to write into the file.
     * @return
     */
    public Boolean createFile (JSONObject data) {

        try{
            HederaTransactionAndQueryDefaults txDef = this.getTxQueryDefaults("1006");

            HederaFile file = new HederaFile(new HederaTransactionID(txDef.payingAccountID));
            file.setNode(txDef.node);
            byte[] bytes = data.toJSONString().getBytes();
            var defaults = new HederaFileCreateDefaults();

            HederaTransactionResult result = file.createNoRecord(
                    0, 0, 1006,
                    0,0,3,
                    32936*2,"File creation",
                    Instant.now().plusSeconds(1), bytes, txDef.payingKeyPair);

            // was it successful ?
            if (result.getPrecheckResult() == HederaPrecheckResult.OK) {
                // yes, get a receipt for the transaction
                HederaTransactionReceipt receipt  = Utilities.getReceipt(
                        result.hederaTransactionID, txDef.node,
                        500, 70, 0);
                if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
                    logger.info("File with number " + file.getFileID().fileNum + " has been created successfully");
                    return true;
                } else {
                    throw new Exception("File creation failed with transactionStatus:" + receipt.transactionStatus.toString());
                }
            } else {
                throw new Exception("File creation failed with getPrecheckResult:" +
                        result.getPrecheckResult().toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

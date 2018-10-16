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
import java.util.Arrays;

@Service
public class FileService extends AbstractBaseService {

    final Logger logger = LoggerFactory.getLogger(FileService.class);

    /**
     * Creates the file in the ledger.
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

            // Meant to be a workaround for a low level SDK bug resulting in a
            // FAIL_INVALID Tx Status according to a hin in Slack. However, it doesn't help.
            Thread.sleep(1500);

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

    /**
     * Creates a file to store the smart contract.
     *
     * @param file
     * @param contents
     * @return
     * @throws Exception
     */
    public HederaFile createSmartContract(HederaFile file, byte[] contents) throws Exception {

        // new file
        long shardNum = 0;
        long realmNum = 0;

        logger.info("");
        logger.info("FILE CREATE");
        logger.info("");

        int fileChunkSize = 3000;
        int position = fileChunkSize;

        byte[] fileChunk = Arrays.copyOfRange(contents, 0, Math.min(fileChunkSize, contents.length));
        logger.info("");
        logger.info("fileChunk:" + Math.min(fileChunkSize, contents.length));

        // createSmartContract the new file
        // file creation transaction
        HederaTransactionResult createResult = file.create(shardNum, realmNum, fileChunk, null);
        // was it successful ?
        if (createResult.getPrecheckResult() == HederaPrecheckResult.OK) {
            // yes, get a receipt for the transaction
            HederaTransactionReceipt receipt  = Utilities.getReceipt(file.hederaTransactionID,  file.txQueryDefaults.node);
            // was that successful ?
            if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
                // yes, get the new account number from the receipt
                file.fileNum = receipt.fileID.fileNum;
                // and print it out
                logger.info("===>Your new file number is " + file.fileNum);

                while (position <= contents.length) {

                    int toPosition = Math.min(position + fileChunkSize, contents.length + 1);
                    byte[] appendChunk = Arrays.copyOfRange(contents, position, toPosition);

                    logger.info("Appending remaining data");
                    if (file.append(appendChunk) != null) {
                        position += fileChunkSize;
                    }
                    else {
                        System.err.println("Appending Failure");
                        System.exit(0);
                    }
                }
            } else {
                logger.info("Failed with transactionStatus:" + receipt.transactionStatus);
                return null;
            }
        } else {
            logger.info("Failed with getPrecheckResult:" + createResult.getPrecheckResult());
            return null;
        }
        return file;
    }
}

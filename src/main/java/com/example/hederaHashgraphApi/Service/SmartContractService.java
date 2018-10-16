package com.example.hederaHashgraphApi.Service;

import com.hedera.sdk.common.*;
import com.hedera.sdk.contract.HederaContract;
import com.hedera.sdk.file.HederaFile;
import com.hedera.sdk.transaction.HederaTransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class SmartContractService extends AbstractBaseService
{
    @Autowired
    private FileService fileService;

    @Autowired
    ResourceLoader resourceLoader;
    private HederaContract create(HederaContract contract, HederaFileID fileID, long initialBalance, byte[] constParams) throws Exception {
        // new contract
        long shardNum = 0;
        long realmNum = 0;
        long gas = 250000000l;
        byte[] constructorParameters = constParams;
        HederaDuration autoRenewPeriod = new HederaDuration(60, 10);

        //fee 100
        contract.txQueryDefaults.node.contractCreateTransactionFee = 100;
        contract.txQueryDefaults.generateRecord = true;



        // createSmartContract the new contract
        // contract creation transaction
        HederaTransactionResult createResult = contract.create(shardNum, realmNum, fileID, initialBalance, gas,
                constructorParameters, autoRenewPeriod);
        // was it successful ?
        if (createResult.getPrecheckResult() == HederaPrecheckResult.OK) {
            // yes, get a receipt for the transaction
            HederaTransactionReceipt receipt = Utilities.getReceipt(contract.hederaTransactionID,
                    contract.txQueryDefaults.node);
            // was that successful ?
            if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
                contract.contractNum = receipt.contractID.contractNum;
                // and print it out
                HederaTransactionRecord record = new HederaTransactionRecord(contract.hederaTransactionID, contract.txQueryDefaults.node.contractGetRecordsQueryFee, contract.txQueryDefaults);
            } else {
                return null;
            }
        } else {
            return null;
        }
        return contract;
    }

    public  Boolean create(String accountId){
        try{
            HederaTransactionAndQueryDefaults txQueryDefaults = this.getTxQueryDefaults(accountId);

            // new account objects
            //HederaAccount account = new HederaAccount(new HederaTransactionID(txQueryDefaults.payingAccountID));

            // setup transaction/query defaults (durations, etc...)
            //account.txQueryDefaults = txQueryDefaults;
            //account.setNode(txQueryDefaults.node);

            txQueryDefaults.fileWacl = txQueryDefaults.payingKeyPair;
            // createSmartContract a file
            // new file object
            HederaFile file = new HederaFile();
            // setup transaction/query defaults (durations, etc...)
            file.txQueryDefaults = txQueryDefaults;

            // get file contents
            InputStream is = resourceLoader.getResource(
                    "classpath:data/smartContract.bin").getInputStream();
            //InputStream is = SmartContractService.class.getResourceAsStream("/main/resources/simpleStorage.bin");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] fileContents = buffer.toByteArray();

            // createSmartContract a file with contents
            file = fileService.createSmartContract(file, fileContents);

            // new contract object
            HederaContract contract = new HederaContract();
            // setup transaction/query defaults (durations, etc...)
            contract.txQueryDefaults = txQueryDefaults;

            // createSmartContract a contract
            contract = create(contract, file.getFileID(), 0, new byte[0]);
            return contract != null;
        }
        catch(Exception e){
            return false;
        }
    }
}

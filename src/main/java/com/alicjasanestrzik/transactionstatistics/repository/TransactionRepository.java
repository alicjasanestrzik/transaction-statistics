package com.alicjasanestrzik.transactionstatistics.repository;

import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionRepository {

    private List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<>());

    public void add(@Valid Transaction transaction) {
        transactionList.add(transaction);
    }

    public synchronized List<Transaction> returnTransactionsToCalculate() {
        ZonedDateTime validationTimeStamp = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(60);
        return transactionList.stream()
                .filter(transaction ->
                        transaction.getTimestamp().isAfter(validationTimeStamp) ||
                                transaction.getTimestamp().isEqual(validationTimeStamp)
                )
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionList() {
        return this.transactionList;
    }

    public boolean isEmpty() {
        return transactionList.isEmpty();
    }
}

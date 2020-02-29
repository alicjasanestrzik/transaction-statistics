package com.alicjasanestrzik.transactionstatistics.repository;

import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class TransactionRepository {

    private List<Transaction> transactionList = Collections.synchronizedList(new LinkedList<>());

    public void add(@Valid Transaction transaction) {
        transactionList.add(transaction);
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}

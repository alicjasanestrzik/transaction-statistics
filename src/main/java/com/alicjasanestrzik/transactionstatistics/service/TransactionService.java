package com.alicjasanestrzik.transactionstatistics.service;

import com.alicjasanestrzik.transactionstatistics.exception.TransactionInTheFutureException;
import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import com.alicjasanestrzik.transactionstatistics.model.TransactionDTO;
import com.alicjasanestrzik.transactionstatistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private StatisticService statisticService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              StatisticService statisticService) {
        this.transactionRepository = transactionRepository;
        this.statisticService = statisticService;
    }

    public synchronized void addTransaction(TransactionDTO transactionToAdd) {
        //if parse is not possible, NumberFormatException will be thrown
        BigDecimal amount =  BigDecimal.valueOf(transactionToAdd.getAmount());

        //if the result exceeds the supported range, DateTimeParseException will be thrown
        ZonedDateTime timestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(transactionToAdd.getTimestamp()), ZoneId.systemDefault());
        validateTransactionTimestamp(timestamp);

        Transaction transaction = new Transaction(amount, timestamp);
        statisticService.calculateStatisticsForAdd(transaction, transactionRepository.isEmpty());
        transactionRepository.add(transaction);
    }

    private void validateTransactionTimestamp(ZonedDateTime timestamp) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        //check if transaction is not in the future
        if (now.isBefore(timestamp)) {
            throw new TransactionInTheFutureException();
        }
    }

    public StatisticDTO getStatistics() {
        return statisticService.getStatistics();
    }
}

package com.alicjasanestrzik.transactionstatistics.service;

import com.alicjasanestrzik.transactionstatistics.event.AddTransactionEvent;
import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import com.alicjasanestrzik.transactionstatistics.repository.TransactionRepository;
import com.alicjasanestrzik.transactionstatistics.util.StatisticsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StatisticService {

    private TransactionRepository transactionRepository;
    private StatisticDTO statistics;

    @Autowired
    public StatisticService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @EventListener
    public void calculateStatistics(AddTransactionEvent addTransactionEvent) {
        statistics = calculate(transactionRepository.returnTransactionsToCalculate());
    }

    @Scheduled(fixedRate=1000)
    private synchronized void calculateTransactionStatisticsFromLast60Seconds() {
        List<Transaction> transactionsFromLastMinute = transactionRepository.returnTransactionsToCalculate();
        statistics = calculate(transactionsFromLastMinute);
        cleanTransactionsOlderThan60Seconds(transactionsFromLastMinute);
    }

    private void cleanTransactionsOlderThan60Seconds(List<Transaction> transactionsFromLastMinute) {
        if (!transactionsFromLastMinute.isEmpty()) {
            transactionRepository.getTransactionList().retainAll(transactionsFromLastMinute);
        }
    }

    private StatisticDTO calculate(List<Transaction> transactions) {

        long count = transactions.size();
        BigDecimal sum = StatisticsCalculator.calculateSum(transactions);
        BigDecimal avg = StatisticsCalculator.calculateAvg(sum, count);
        BigDecimal min = StatisticsCalculator.calculateMin(transactions);
        BigDecimal max = StatisticsCalculator.calculateMax(transactions);

        return new StatisticDTO(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);

    }
    public StatisticDTO getStatistics() {
        return statistics;
    }
}

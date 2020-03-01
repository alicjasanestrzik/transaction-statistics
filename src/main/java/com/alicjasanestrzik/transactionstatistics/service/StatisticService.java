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
    public synchronized void calculateStatistics(AddTransactionEvent event) {
        List<Transaction> transactionList = transactionRepository.returnTransactionsToCalculate();
        statistics = calculate(transactionList);
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

    public StatisticDTO getStatistics() {
        return statistics;
    }

    private StatisticDTO calculate(List<Transaction> transactionList) {
        long count = transactionList.size();
        BigDecimal sum = StatisticsCalculator.calculateSum(transactionList);
        BigDecimal avg = StatisticsCalculator.calculateAvg(sum, count);
        BigDecimal min = StatisticsCalculator.calculateMin(transactionList);
        BigDecimal max = StatisticsCalculator.calculateMax(transactionList);

        return new StatisticDTO(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);

    }
}

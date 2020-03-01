package com.alicjasanestrzik.transactionstatistics.service;

import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import com.alicjasanestrzik.transactionstatistics.repository.TransactionRepository;
import com.alicjasanestrzik.transactionstatistics.util.StatisticsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
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

    public synchronized void calculateStatisticsForAdd(Transaction transaction, boolean isTransactionListEmpty) {
        BigDecimal sum = StatisticsCalculator.calculateSum(transaction.getAmount(), statistics);
        long count = statistics.getCount() + 1;
        BigDecimal avg = StatisticsCalculator.calculateAvg(sum, count);
        BigDecimal min = StatisticsCalculator.calculateMin(transaction.getAmount(), statistics, isTransactionListEmpty);
        BigDecimal max = StatisticsCalculator.calculateMax(transaction.getAmount(), statistics);

        statistics = new StatisticDTO(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);
    }

    @Scheduled(fixedRate=1000)
    private synchronized void calculateTransactionStatisticsFromLast60Seconds() {
        List<Transaction> transactionsFromLastMinute = transactionRepository.returnTransactionsToCalculate();
        statistics = calculateForList(transactionsFromLastMinute);
        cleanTransactionsOlderThan60Seconds(transactionsFromLastMinute);
    }

    private StatisticDTO calculateForList(List<Transaction> transactionList) {
        long count = transactionList.size();
        BigDecimal sum = StatisticsCalculator.calculateSum(transactionList);
        BigDecimal avg = StatisticsCalculator.calculateAvg(sum, count);
        BigDecimal min = StatisticsCalculator.calculateMin(transactionList);
        BigDecimal max = StatisticsCalculator.calculateMax(transactionList);

        return new StatisticDTO(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);
    }

    private void cleanTransactionsOlderThan60Seconds(List<Transaction> transactionsFromLastMinute) {
        if (!transactionsFromLastMinute.isEmpty()) {
            transactionRepository.getTransactionList().retainAll(transactionsFromLastMinute);
        }
    }

    public synchronized StatisticDTO getStatistics() {
        return statistics;
    }
}

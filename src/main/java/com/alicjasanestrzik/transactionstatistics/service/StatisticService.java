package com.alicjasanestrzik.transactionstatistics.service;

import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import com.alicjasanestrzik.transactionstatistics.util.StatisticsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StatisticService {

    private TransactionService transactionService;

    @Autowired
    public StatisticService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public StatisticDTO calculateStatistics() {
        List<Transaction> transactionList = transactionService.returnTransactionsToCalculate();

        long count = transactionList.size();
        BigDecimal sum = StatisticsCalculator.calculateSum(transactionList);
        BigDecimal avg = StatisticsCalculator.calculateAvg(sum, count);
        BigDecimal min = StatisticsCalculator.calculateMin(transactionList);
        BigDecimal max = StatisticsCalculator.calculateMax(transactionList);

        return new StatisticDTO(sum.doubleValue(), avg.doubleValue(), max.doubleValue(), min.doubleValue(), count);
    }

}

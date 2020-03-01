package com.alicjasanestrzik.transactionstatistics.util;

import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class StatisticsCalculator {

    private StatisticsCalculator() {}

    public static BigDecimal calculateSum(List<Transaction> transactionList) {
        BigDecimal sum = transactionList.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateSum(BigDecimal amount, StatisticDTO statisticDTO) {
        BigDecimal sum = BigDecimal.valueOf(statisticDTO.getSum()).add(amount);

        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateAvg(BigDecimal sum, long count) {
        return count > 0 ?
                sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP)
                : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateMin(List<Transaction> transactionList) {
        BigDecimal min = transactionList.stream()
                .map(Transaction::getAmount)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        return min.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateMin(BigDecimal amount, StatisticDTO statisticDTO, boolean isTransactionListEmpty) {
        if (isTransactionListEmpty) {
            return amount;
        }
        
        BigDecimal currentMinValue = BigDecimal.valueOf(statisticDTO.getMin());
        BigDecimal min = amount.compareTo(currentMinValue) < 0 ? amount : currentMinValue;

        return min.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateMax(List<Transaction> transactionList) {
        BigDecimal min = transactionList.stream()
                .map(Transaction::getAmount)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        return min.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateMax(BigDecimal amount, StatisticDTO statisticDTO) {
        BigDecimal currentMaxValue = BigDecimal.valueOf(statisticDTO.getMax());
        BigDecimal max = amount.compareTo(currentMaxValue) > 0 ? amount : currentMaxValue;

        return max.setScale(2, RoundingMode.HALF_UP);
    }
}
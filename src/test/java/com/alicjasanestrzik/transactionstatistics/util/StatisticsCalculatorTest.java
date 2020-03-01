package com.alicjasanestrzik.transactionstatistics.util;

import com.alicjasanestrzik.transactionstatistics.model.StatisticDTO;
import com.alicjasanestrzik.transactionstatistics.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCalculatorTest {

    @Test
    public void calculateSum() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(2)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(3)));

        BigDecimal expected = BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateSum(transactionList));
    }

    @Test
    public void calculateSumForAdd() {
        StatisticDTO statisticDTO = new StatisticDTO(2, 2, 2, 0, 1);
        BigDecimal amount = BigDecimal.valueOf(3);

        BigDecimal sum = StatisticsCalculator.calculateSum(amount, statisticDTO);

        BigDecimal expected = BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, sum);
    }

    @Test
    public void calculateSumWithOnlyPositiveNumbers() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(2.50)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(3)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1000)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(450.50)));

        BigDecimal expected = BigDecimal.valueOf(1456).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateSum(transactionList));
    }

    @Test
    public void calculateSumWithNegativeNumbers() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-345.50)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(488.00)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-500.00)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(250.80)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-210.05)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-200.50)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1.00)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(3)));

        BigDecimal expected = BigDecimal.valueOf(-513.25).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateSum(transactionList));
    }

    @Test
    public void calculateAvgForPositiveSum() {
        BigDecimal expected = new BigDecimal(2).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateAvg(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP), 5));
    }

    @Test
    public void calculateSumIfTransactionListIsEmpty() {
        BigDecimal expected = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateSum(new LinkedList<>()));
    }

    @Test
    public void calculateAvgWithNegativeSum() {
        BigDecimal expected = BigDecimal.valueOf(-57.6).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateAvg(BigDecimal.valueOf(-288).setScale(2, RoundingMode.HALF_UP), 5));
    }

    @Test
    public void calculateAvgWithZeroSum() {
        BigDecimal expected = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateAvg(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), 5));
    }

    @Test
    public void calculateAvgDivideByZero() {
        BigDecimal expected = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateAvg(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP), 0));
    }

    @Test
    public void calculateMin() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(38)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1000)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(450.50)));

        BigDecimal expected = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMin(transactionList));
    }

    @Test
    public void calculateMinWithNegativeNumbers() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-11)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(38)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-1000)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(450.50)));

        BigDecimal expected = BigDecimal.valueOf(-1000).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMin(transactionList));
    }

    @Test
    public void calculateMinIfTransactionListIsEmpty() {
        BigDecimal expected = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMin(new LinkedList<>()));
    }

    @Test
    public void calculateMinSecondImplementationEmptyTransactionList() {
        BigDecimal expected = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);
        StatisticDTO statisticDTO = new StatisticDTO(0, 0, 0, 0, 0);
        BigDecimal transactionAmount = BigDecimal.valueOf(1);

        assertEquals(expected, StatisticsCalculator.calculateMin(transactionAmount, statisticDTO, true));
    }

    @Test
    public void calculateMinSecondImplementationNotEmptyTransactionList() {
        BigDecimal expected = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        StatisticDTO statisticDTO = new StatisticDTO(2, 2, 2, 0, 1);
        BigDecimal transactionAmount = BigDecimal.valueOf(1);
        boolean isTransactionListEmpty = false;

        assertEquals(expected, StatisticsCalculator.calculateMin(transactionAmount, statisticDTO, false));
    }

    @Test
    public void calculateMax() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(11)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(38)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1000)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(450.50)));

        BigDecimal expected = BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMax(transactionList));
    }

    @Test
    public void calculateMaxSecondImplementation() {
        BigDecimal expected = BigDecimal.valueOf(3).setScale(2, RoundingMode.HALF_UP);
        StatisticDTO statisticDTO = new StatisticDTO(4, 2, 2, 2, 2);
        BigDecimal transactionAmount = BigDecimal.valueOf(3);

        assertEquals(expected, StatisticsCalculator.calculateMax(transactionAmount, statisticDTO));
    }

    @Test
    public void calculateMaxWithNegativeNumbers() {
        List<Transaction> transactionList = new LinkedList<>();
        transactionList.add(prepareTransaction(BigDecimal.valueOf(-1100)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(38)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(1024)));
        transactionList.add(prepareTransaction(BigDecimal.valueOf(450.50)));

        BigDecimal expected = BigDecimal.valueOf(1024).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMax(transactionList));
    }

    @Test
    public void calculateMaxIfTransactionListIsEmpty() {
        BigDecimal expected = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, StatisticsCalculator.calculateMax(new LinkedList<>()));
    }


    private Transaction prepareTransaction(BigDecimal amount) {
        return new Transaction(amount, ZonedDateTime.now());
    }

}
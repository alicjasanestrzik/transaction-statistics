package com.alicjasanestrzik.transactionstatistics.model;

public class StatisticDTO {
    double sum;
    double avg;
    double max;
    double min;
    long count;

    public StatisticDTO(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }
}
package com.alicjasanestrzik.transactionstatistics.model;

public class StatisticDTO {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public StatisticDTO(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }
}
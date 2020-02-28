package com.alicjasanestrzik.transactionstatistics.model;

import javax.validation.constraints.NotNull;

public class TransactionDTO {

    @NotNull
    private Double amount;

    @NotNull
    private Long timestamp;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}

package com.alicjasanestrzik.transactionstatistics.model;

import javax.validation.constraints.NotNull;

public class TransactionDTO {

    @NotNull
    private Double amount;

    @NotNull
    private Long timestamp;

    public Double getAmount() {
        return amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}

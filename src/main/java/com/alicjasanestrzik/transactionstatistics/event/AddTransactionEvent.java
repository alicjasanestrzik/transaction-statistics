package com.alicjasanestrzik.transactionstatistics.event;

import org.springframework.context.ApplicationEvent;

public class AddTransactionEvent extends ApplicationEvent {

    public AddTransactionEvent(Object source) {
        super(source);
    }
}

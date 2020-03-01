package com.alicjasanestrzik.transactionstatistics.controller;

import com.alicjasanestrzik.transactionstatistics.event.AddTransactionEvent;
import com.alicjasanestrzik.transactionstatistics.exception.TransactionInTheFutureException;
import com.alicjasanestrzik.transactionstatistics.exception.TransactionTooOldException;
import com.alicjasanestrzik.transactionstatistics.model.TransactionDTO;
import com.alicjasanestrzik.transactionstatistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;

@RestController
public class TransactionController {

    private TransactionService transactionService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 ApplicationEventPublisher applicationEventPublisher) {
        this.transactionService = transactionService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(path = "/transaction")
    @ResponseBody
    public ResponseEntity<HttpStatus> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        try {
            transactionService.addTransaction(transactionDTO);
            AddTransactionEvent addTransactionEvent = new AddTransactionEvent(this);
            applicationEventPublisher.publishEvent(addTransactionEvent);
        } catch (NumberFormatException | DateTimeParseException | TransactionInTheFutureException ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); //422
        } catch (TransactionTooOldException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
        }
        return new ResponseEntity<>(HttpStatus.CREATED); //201
    }
}
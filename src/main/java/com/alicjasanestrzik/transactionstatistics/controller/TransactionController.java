package com.alicjasanestrzik.transactionstatistics.controller;

import com.alicjasanestrzik.transactionstatistics.exception.TransactionInTheFutureException;
import com.alicjasanestrzik.transactionstatistics.exception.TransactionTooOldException;
import com.alicjasanestrzik.transactionstatistics.model.TransactionDTO;
import com.alicjasanestrzik.transactionstatistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping(path = "/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<HttpStatus> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        try {
            transactionService.addTransaction(transactionDTO);
        } catch (NumberFormatException | DateTimeParseException | TransactionInTheFutureException ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); //422
        } catch (TransactionTooOldException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
        }
        return new ResponseEntity<>(HttpStatus.CREATED); //201
    }

}
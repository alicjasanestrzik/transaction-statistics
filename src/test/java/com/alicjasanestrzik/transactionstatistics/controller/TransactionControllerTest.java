package com.alicjasanestrzik.transactionstatistics.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testFor100ValidTransactions() throws Exception {

        List<String> jsonTransactionsRequests = getJsonRequests("100ValidTransactions.json");
        long offset = -30000;
        for(String json : jsonTransactionsRequests) {
            sendTransactionRequestAndCheckIfSuccess(offset, json);
            offset += 100;
        }

        String mvcResult = sendStatisticsRequestAndGetResponse();

        List<String> expected_jsonStatisticsResult = getJsonRequests("100ValidTransactionsStatistics.json");
        assertEquals(expected_jsonStatisticsResult.get(0), mvcResult);
    }

    private List<String> getJsonRequests(String filename) {
        ClassPathResource jsonResource = new ClassPathResource(filename);
        try (InputStream inputStream = jsonResource.getInputStream()) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    private void sendTransactionRequestAndCheckIfSuccess(long offset, String line) throws Exception {
        JSONObject json = new JSONObject(line);
        json.put("timestamp", getNowMillisecondsWithOffset(offset));
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .content(json.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private String sendStatisticsRequestAndGetResponse() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private long getNowMillisecondsWithOffset(long offset) {
        return Instant.now().plusMillis(offset).toEpochMilli();
    }

}
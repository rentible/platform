package hu.lanoga.flatshares.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChangellyServiceImp implements ChangellyService {

    @Value("${changelly.api.key}")
    private String apiKey;

    @Value("${changelly.api.secret}")
    private String apiSecret;

    @Value("${changelly.api.url}")
    private String apiUrl;


    @Override
    public void getCurrencies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getCurrenciesFull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getMinAmount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getExchangeAmount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createTransaction() {
        throw new UnsupportedOperationException();
    }
}

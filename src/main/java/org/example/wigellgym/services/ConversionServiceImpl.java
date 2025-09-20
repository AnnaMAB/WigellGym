package org.example.wigellgym.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ConversionServiceImpl implements ConversionService {

    private final RestClient restClient;

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }
    //TODO ------------------------kasta!   Logga?--------Lägg in container adressen
    @Override
    public double getConversionRate() {
        Double currentRate = restClient.get()
                .uri("http://localhost:8082/convert/sek2euro")
                .retrieve()
                .body(Double.class);
        return currentRate;
    }
}

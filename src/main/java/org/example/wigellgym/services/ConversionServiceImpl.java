package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Objects;

@Service
public class ConversionServiceImpl implements ConversionService {

    private final RestClient restClient;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }
    //TODO ------------------------kasta!   Logga?--------Lägg in container adressen

    public Double getConversionRateOld() {
        Double currentRate = restClient.get()
                .uri("http://localhost:8082/convert/sek2euro")
                .retrieve()
                .body(Double.class);

        //TODO --- funkar inte programmet kraschar ändå. Testa Optional?
        return Objects.requireNonNullElse(currentRate, 0.0);    //Det här är så IntelliJ ville att jag skulle skriva
    }

    @Override
    public Double getConversionRate() {
        Double currentRate;
        try {
            currentRate = restClient.get()
                .uri("http://localhost:8082/convert/sek2euro")
                .retrieve()
                .body(Double.class);

        } catch (RestClientException e) {
            System.err.println("API unreachable euro price set to 0");
            currentRate = 0.0;
        }
        return currentRate;
    }




}

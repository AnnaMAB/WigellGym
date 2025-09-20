package org.example.wigellgym.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class ConversionServiceImpl implements ConversionService {

    private final RestClient restClient;

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }
    //TODO ------------------------kasta!   Logga?--------Lägg in container adressen
    @Override
    public Double getConversionRate() {
        Double currentRate = restClient.get()
                .uri("http://localhost:8082/convert/sek2euro")
                .retrieve()
                .body(Double.class);

        //TODO --- funkar inte programmet kraschar ändå. Testa Optional?
        return Objects.requireNonNullElse(currentRate, 0.0);    //Det här är så IntelliJ ville att jag skulle skriva

    }
}

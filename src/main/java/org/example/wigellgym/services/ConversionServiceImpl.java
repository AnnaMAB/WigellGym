package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


@Service
public class ConversionServiceImpl implements ConversionService {

    private final RestClient restClient;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }


    @Override
    public Double getConversionRate() {
        Double currentRate;
        try {
            currentRate = restClient.get()
                //.uri("http://localhost:8082/convert/sek2euro")
                .uri("http://euro-converter:8082/convert/sek2euro")               //TODO --------Lägg in container adressen
                .retrieve()
                .body(Double.class);

        } catch (RestClientException e) {                                           // Metoden som kallat på getConversionRate() får
            F_LOG.error("API unreachable, euro conversion rate set to 0");          // bestämma om den vill kasta eller köra vidare med 0.0
            currentRate = 0.0;
        }
        return currentRate;
    }

}

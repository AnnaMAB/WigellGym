package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


@Service
public class ConversionServiceImpl implements ConversionService {

    private final RestClient restClient;
    private static final Logger F_LOG = LogManager.getLogger("functionality");
    @Value("${converter.api.url}")
    private String converterApiUrl;

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }


    @Override
    public Double getConversionRate() {
        try {
            ResponseEntity<Double> response = restClient.get()
                .uri(converterApiUrl)
                .retrieve()
                .toEntity(Double.class);


            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                F_LOG.info("Conversion rate fetched successfully: {}", response.getBody());
                return response.getBody();
            } else {
                F_LOG.warn("Conversion rate fetching failed: {}", response.getStatusCode());
                throw new IllegalStateException(
                        response.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            F_LOG.warn("ConversionRate API unreachable");
            throw new IllegalStateException("Failed to fetch conversion rate", e);
        }
    }

}

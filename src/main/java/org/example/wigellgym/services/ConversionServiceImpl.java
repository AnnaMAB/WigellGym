package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
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
    private final AuthInfo authInfo;

    @Autowired
    public ConversionServiceImpl(RestClient.Builder restClientBuilder, AuthInfo authInfo) {
        this.restClient = restClientBuilder.build();
        this.authInfo = authInfo;
    }


    @Override
    public Double getConversionRate() {
        String role = authInfo.getRole();
        try {
            ResponseEntity<Double> response = restClient.get()
                .uri(converterApiUrl)
                .retrieve()
                .toEntity(Double.class);


            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                F_LOG.info("{} successfully fetched conversion rate: {}", role, response.getBody());
                return response.getBody();
            } else {
                F_LOG.warn("{}: Conversion rate fetching failed: {}", role, response.getStatusCode());
                throw new IllegalStateException(
                        response.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            F_LOG.warn("{}: ConversionRate API unreachable", role);
            throw new IllegalStateException("Failed to fetch conversion rate", e);
        }
    }

}

package ru.zaikin.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.zaikin.manager.client.RestProductsRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestProductsRestClient restProductsRestClient(
            @Value("${zaikin.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new RestProductsRestClient(
                RestClient.builder()
                        .baseUrl(catalogueBaseUri)
                        .build());
    }

}

package com.test.elasticsearch.config;

import com.test.elasticsearch.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.http.HttpStatus;

@Configuration
@Slf4j
public class ElasticSearchConfig {
    @Bean
    public RestHighLevelClient client() {
            ClientConfiguration clientConfiguration
                    = ClientConfiguration.builder()
                    .connectedTo("localhost:9200")
                    .build();

            return RestClients.create(clientConfiguration).rest();
    }
}

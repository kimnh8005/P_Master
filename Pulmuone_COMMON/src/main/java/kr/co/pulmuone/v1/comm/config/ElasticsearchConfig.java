package kr.co.pulmuone.v1.comm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hosts}")
    private String[] hosts;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {

        return new RestHighLevelClient(
                RestClient.builder(
                        Arrays.stream(hosts).map(HttpHost::create).toArray(HttpHost[]::new)
                )
        );
    }

}
package com.vlinvestment.accountservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SNSClientConfig {

    @Value("${cloud.aws.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${cloud.aws.region}")
    private String region;

    @Primary
    @Bean
    public AmazonSNS amazonSNS() {
        var credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        return AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}

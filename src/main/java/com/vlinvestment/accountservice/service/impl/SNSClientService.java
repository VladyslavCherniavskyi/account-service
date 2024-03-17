package com.vlinvestment.accountservice.service.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SNSClientService {

    private final AmazonSNS snsClient;

    public void pubTextSMS(String phone, String message) {
        var request = new PublishRequest()
                .withPhoneNumber(phone)
                .withMessage(message);

        snsClient.publish(request);
    }

}

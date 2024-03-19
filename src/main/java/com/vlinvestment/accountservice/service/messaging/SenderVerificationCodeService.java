package com.vlinvestment.accountservice.service.messaging;

public interface SenderVerificationCodeService {

    String sendVerificationCode(MessagingSource messagingSource, String source);

}

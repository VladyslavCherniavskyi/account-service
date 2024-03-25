package com.vlinvestment.accountservice.service;

import com.vlinvestment.accountservice.entity.AccessCode;

public interface AccessCodeService {

    AccessCode create(AccessCode accessCode);

    boolean existBySource(String source);

    String findCodeBySource(String source);

    void delete(String source);

}

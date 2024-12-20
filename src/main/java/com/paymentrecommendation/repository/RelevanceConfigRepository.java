package com.paymentrecommendation.repository;

import com.paymentrecommendation.enums.LineOfBusiness;
import com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.List;

public interface RelevanceConfigRepository {

    List<PaymentInstrumentType> getRelevanceByLineOfBusiness(LineOfBusiness lineOfBusiness);
    void save(LineOfBusiness lineOfBusiness, List<PaymentInstrumentType> paymentInstrumentTypes);

}
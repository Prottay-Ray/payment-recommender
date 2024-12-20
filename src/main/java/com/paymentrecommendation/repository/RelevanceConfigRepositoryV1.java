package com.paymentrecommendation.repository;

import com.paymentrecommendation.enums.LineOfBusiness;
import com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RelevanceConfigRepositoryV1 implements RelevanceConfigRepository {

    private static final Map<LineOfBusiness, List<PaymentInstrumentType>> lineOfBusinessRelevanceRegistry = new EnumMap<>(LineOfBusiness.class);

    @Override
    public List<PaymentInstrumentType> getRelevanceByLineOfBusiness(LineOfBusiness lineOfBusiness) {
        return lineOfBusinessRelevanceRegistry.getOrDefault(lineOfBusiness, Collections.emptyList());
    }

    @Override
    public void save(LineOfBusiness lineOfBusiness,
                     List<PaymentInstrumentType> paymentInstrumentTypes) {
        lineOfBusinessRelevanceRegistry.put(lineOfBusiness, paymentInstrumentTypes);
    }
}
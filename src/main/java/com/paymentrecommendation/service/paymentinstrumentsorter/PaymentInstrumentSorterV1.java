package com.paymentrecommendation.service.paymentinstrumentsorter;

import com.paymentrecommendation.enums.LineOfBusiness;
import com.paymentrecommendation.enums.PaymentInstrumentType;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentSorterContext;
import com.paymentrecommendation.repository.RelevanceConfigRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentInstrumentSorterV1 implements PaymentInstrumentSorter {

    private final RelevanceConfigRepository relevanceConfigRepository;

    public PaymentInstrumentSorterV1(RelevanceConfigRepository relevanceConfigRepository) {
        this.relevanceConfigRepository = relevanceConfigRepository;
    }

    @Override
    public List<PaymentInstrument> getSorted(PaymentInstrumentSorterContext context) {
        List<PaymentInstrument> paymentInstrumentList = context.getFetchedPaymentInstrumentList();
        LineOfBusiness lineOfBusiness = context.getCart().getLineOfBusiness();
        List<PaymentInstrumentType> relevance = relevanceConfigRepository.getRelevanceByLineOfBusiness(lineOfBusiness);
        return paymentInstrumentList.stream()
                .sorted(Comparator
                        .comparingInt((PaymentInstrument paymentInstrument)
                                -> relevance.indexOf(paymentInstrument.getPaymentInstrumentType()))
                        .thenComparingDouble(paymentInstrument -> -paymentInstrument.getRelevanceScore())) // Negate to sort descending
                .collect(Collectors.toList());
    }
}
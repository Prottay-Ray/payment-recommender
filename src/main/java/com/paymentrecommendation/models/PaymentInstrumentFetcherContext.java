package com.paymentrecommendation.models;

import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.service.paymentinstrumentfetcher.PaymentInstrumentFetcher;
import lombok.*;

/**
 * PaymentInstrumentContext to store all relevant info required to fetch PaymentInstruments.
 * Using this class to avoid raw use of parameters in method signature as any extension in number of parameters
 * in the future would result in a painful refactoring at all places of use and not so pretty practice
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInstrumentFetcherContext {

    private User user;
    private Cart cart;
    private PaymentInstrumentFetcher alternateBasePaymentInstrumentFetcher = PaymentInstrumentFetcher.NULL_OBJECT;
    private PaymentInstrumentFetcherType replaceableBaseFetcherType = PaymentInstrumentFetcherType.NONE;

    public UserContext getUserContext() {
        return user.getUserContext();
    }
}
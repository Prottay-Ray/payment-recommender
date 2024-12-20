package com.paymentrecommendation.service.paymentinstrumentfetcher;

import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;

import java.util.List;

import static com.paymentrecommendation.enums.PaymentInstrumentFetcherType.ACTIVE;

public class ActivePaymentInstrumentFetcher implements PaymentInstrumentFetcher {

    private PaymentInstrumentFetcher paymentInstrumentFetcher;

    public ActivePaymentInstrumentFetcher(PaymentInstrumentFetcherLocator locator) {
        locator.registerPaymentInstrumentFetcher(this);
    }

    @Override
    public List<PaymentInstrument> getInstruments(PaymentInstrumentFetcherContext context) {
        return context.getUser().getUserPaymentInstrument().getPaymentInstruments();
    }

    @Override
    public void setBaseInstrumentFetcher(PaymentInstrumentFetcher paymentInstrumentFetcher) {
        this.paymentInstrumentFetcher = paymentInstrumentFetcher;
    }

    @Override
    public PaymentInstrumentFetcher getBaseInstrumentFetcher() {
        return paymentInstrumentFetcher;
    }

    @Override
    public PaymentInstrumentFetcherType getType() {
        return ACTIVE;
    }
}

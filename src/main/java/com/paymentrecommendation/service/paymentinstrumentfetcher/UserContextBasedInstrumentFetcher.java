package com.paymentrecommendation.service.paymentinstrumentfetcher;

import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.enums.PaymentInstrumentType;
import com.paymentrecommendation.models.DeviceContext;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;
import com.paymentrecommendation.models.UserContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.paymentrecommendation.enums.PaymentInstrumentFetcherType.USER_CONTEXT_BASED;

/**
 * Decorating Instrument Fetcher to have single responsibility of filteration,
 * abstracting out the other filterations requirements required in the chain
 */
public class UserContextBasedInstrumentFetcher implements PaymentInstrumentFetcher {

    private PaymentInstrumentFetcher paymentInstrumentFetcher;

    public UserContextBasedInstrumentFetcher(PaymentInstrumentFetcherLocator locator) {
        locator.registerPaymentInstrumentFetcher(this);
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
        return USER_CONTEXT_BASED;
    }

    @Override
    public List<PaymentInstrument> getInstruments(PaymentInstrumentFetcherContext context) {
        List<PaymentInstrument> instruments = getBasePaymentInstruments(context);
        if (instruments == null) {
            return Collections.emptyList();
        }
        if (shouldSendAllInstruments(context)) {
            return instruments;
        }
        return instruments.stream()
                .filter(paymentInstrument ->
                        paymentInstrument.getPaymentInstrumentType() != PaymentInstrumentType.UPI)
                .collect(Collectors.toList());
    }

    private static boolean shouldSendAllInstruments(PaymentInstrumentFetcherContext context) {
        return context.getUserContext().getDeviceContext().isUpiEnabled()
                || context.getUserContext() == UserContext.EMPTY_CONTEXT
                || context.getUserContext().getDeviceContext() == DeviceContext.EMPTY_CONTEXT;
    }
}

package com.paymentrecommendation.service.paymentinstrumentfetcher;

import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;

import java.util.Collections;
import java.util.List;

import static com.paymentrecommendation.enums.PaymentInstrumentFetcherType.NONE;

/**
 * A SAM Interface, to fetch the list of supported Payment Instruments
 */
public interface PaymentInstrumentFetcher {

    List<PaymentInstrument> getInstruments(PaymentInstrumentFetcherContext context);
    void setBaseInstrumentFetcher(PaymentInstrumentFetcher paymentInstrumentFetcher);
    PaymentInstrumentFetcher getBaseInstrumentFetcher();
    PaymentInstrumentFetcherType getType();

    default List<PaymentInstrument> getBasePaymentInstruments(PaymentInstrumentFetcherContext context) {
        PaymentInstrumentFetcher alternateBasePaymentInstrumentFetcher = context.getAlternateBasePaymentInstrumentFetcher();
        if (getType() == context.getReplaceableBaseFetcherType()
                && alternateBasePaymentInstrumentFetcher != NULL_OBJECT) {
            return alternateBasePaymentInstrumentFetcher.getInstruments(context);
        }
        return getBaseInstrumentFetcher().getInstruments(context);
    }

    // for use in case of default operations, as null should be avoided (null-object pattern)
    PaymentInstrumentFetcher NULL_OBJECT = new PaymentInstrumentFetcher() {
        @Override
        public List<PaymentInstrument> getInstruments(PaymentInstrumentFetcherContext context) {
            return Collections.emptyList();
        }

        @Override
        public void setBaseInstrumentFetcher(PaymentInstrumentFetcher paymentInstrumentFetcher) {}

        @Override
        public PaymentInstrumentFetcher getBaseInstrumentFetcher() {
            return NULL_OBJECT;
        }

        @Override
        public PaymentInstrumentFetcherType getType() {
            return NONE;
        }
    };
}
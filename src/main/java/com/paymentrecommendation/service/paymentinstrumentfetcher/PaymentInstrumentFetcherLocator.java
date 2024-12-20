package com.paymentrecommendation.service.paymentinstrumentfetcher;

import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.models.Cart;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;
import com.paymentrecommendation.models.UserContext;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

import static com.paymentrecommendation.enums.PaymentInstrumentFetcherType.*;
import static com.paymentrecommendation.service.paymentinstrumentfetcher.PaymentInstrumentFetcher.NULL_OBJECT;

// Service locator design pattern (similar to strategy design pattern)
@NoArgsConstructor
public class PaymentInstrumentFetcherLocator {

    private static final Map<PaymentInstrumentFetcherType, PaymentInstrumentFetcher> fetcherRegistry =
            new EnumMap<>(PaymentInstrumentFetcherType.class);

    private PaymentInstrumentFetcher defaultPaymentInstrumentFetcher = NULL_OBJECT;

    public PaymentInstrumentFetcher locate(PaymentInstrumentFetcherContext context) {
        try {
            if (context == null) {
                return NULL_OBJECT;
            }
            return resolveInstrument(context);
        } catch (Exception e) {
            return NULL_OBJECT;
        }
    }

    public void registerPaymentInstrumentFetcher(PaymentInstrumentFetcher fetcher) {
        if (fetcher != null && fetcher.getType() != null) {
            fetcherRegistry.put(fetcher.getType(), fetcher);
        }
    }

    private PaymentInstrumentFetcher resolveInstrument(PaymentInstrumentFetcherContext context) {
        loadDefaultPaymentInstrumentFetcher();
        PaymentInstrumentFetcher paymentInstrumentFetcher = defaultPaymentInstrumentFetcher;
        Cart cart = context.getCart();
        UserContext userContext = context.getUserContext();
        /*
            if based on specific contextual (user segment of cart item based) cases or
            for dividing partial traffic to a new fetcher logic (say introducing a new instrument along with others),
            we can pass an alternative fetcher in the context (PaymentInstrumentContext) according,

            for example for a specific user segment, based on the user context received:

            context.setAlternateBasePaymentInstrumentFetcher(newUserContextBasedFetcher);
            context.setReplaceableBaseFetcherType(USER_CONTEXT_BASED);

         */
        return paymentInstrumentFetcher;
    }

    private void loadDefaultPaymentInstrumentFetcher() {
        if (defaultPaymentInstrumentFetcher == NULL_OBJECT) {
            PaymentInstrumentFetcher allActiveInstrumentsFetcher = fetcherRegistry.getOrDefault(ACTIVE, NULL_OBJECT);
            PaymentInstrumentFetcher userContextBasedFetcher = fetcherRegistry.getOrDefault(USER_CONTEXT_BASED, NULL_OBJECT);
            PaymentInstrumentFetcher cartBasedFetcher = fetcherRegistry.getOrDefault(CART_BASED, NULL_OBJECT);

            allActiveInstrumentsFetcher.setBaseInstrumentFetcher(NULL_OBJECT);
            cartBasedFetcher.setBaseInstrumentFetcher(allActiveInstrumentsFetcher);
            userContextBasedFetcher.setBaseInstrumentFetcher(cartBasedFetcher);
            defaultPaymentInstrumentFetcher = userContextBasedFetcher;
        }
    }
}
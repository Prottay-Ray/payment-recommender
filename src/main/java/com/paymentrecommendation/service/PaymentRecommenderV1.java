package com.paymentrecommendation.service;

import com.paymentrecommendation.exception.NoLineOfBusinessException;
import com.paymentrecommendation.models.Cart;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;
import com.paymentrecommendation.models.PaymentInstrumentSorterContext;
import com.paymentrecommendation.models.User;
import com.paymentrecommendation.repository.RelevanceConfigRepository;
import com.paymentrecommendation.repository.RelevanceConfigRepositoryV1;
import com.paymentrecommendation.service.paymentinstrumentfetcher.ActivePaymentInstrumentFetcher;
import com.paymentrecommendation.service.paymentinstrumentfetcher.CartBasedInstrumentFetcher;
import com.paymentrecommendation.service.paymentinstrumentfetcher.PaymentInstrumentFetcherLocator;
import com.paymentrecommendation.service.paymentinstrumentfetcher.UserContextBasedInstrumentFetcher;
import com.paymentrecommendation.service.paymentinstrumentsorter.PaymentInstrumentSorter;
import com.paymentrecommendation.service.paymentinstrumentsorter.PaymentInstrumentSorterV1;

import java.util.Arrays;
import java.util.List;

import static com.paymentrecommendation.enums.LineOfBusiness.*;
import static com.paymentrecommendation.enums.PaymentInstrumentType.*;

public class PaymentRecommenderV1 implements PaymentRecommender {

    private final PaymentInstrumentSorter paymentInstrumentSorter;
    private final PaymentInstrumentFetcherLocator serviceLocator;

    public PaymentRecommenderV1() {
        RelevanceConfigRepository relevanceConfigRepository = new RelevanceConfigRepositoryV1();
        relevanceConfigRepository.save(CREDIT_CARD_BILL_PAYMENT, Arrays.asList(UPI, NETBANKING, DEBIT_CARD));
        relevanceConfigRepository.save(COMMERCE, Arrays.asList(CREDIT_CARD, UPI, DEBIT_CARD));
        relevanceConfigRepository.save(INVESTMENT, Arrays.asList(UPI, NETBANKING, DEBIT_CARD));
        serviceLocator = new PaymentInstrumentFetcherLocator();
        new ActivePaymentInstrumentFetcher(serviceLocator);
        new CartBasedInstrumentFetcher(serviceLocator);
        new UserContextBasedInstrumentFetcher(serviceLocator);
        paymentInstrumentSorter = new PaymentInstrumentSorterV1(relevanceConfigRepository);
    }

    @Override
    public List<PaymentInstrument> recommendPaymentInstruments(User user, Cart cart) {
        vaidateRecommendPaymentInstrumentsRequest(user, cart);
        PaymentInstrumentFetcherContext fetcherContext = PaymentInstrumentFetcherContext.builder()
                .user(user)
                .cart(cart)
                .build();
        List<PaymentInstrument> paymentInstruments =
                serviceLocator
                .locate(fetcherContext)
                .getInstruments(fetcherContext);
        return paymentInstrumentSorter.getSorted(new PaymentInstrumentSorterContext(user, cart, paymentInstruments));
    }

    private void vaidateRecommendPaymentInstrumentsRequest(User user, Cart cart) {
        if (cart.getLineOfBusiness() == null) {
            throw new NoLineOfBusinessException();
        }
    }
}
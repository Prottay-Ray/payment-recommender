package com.paymentrecommendation.service.paymentinstrumentfetcher;

import com.paymentrecommendation.enums.LineOfBusiness;
import com.paymentrecommendation.enums.PaymentInstrumentFetcherType;
import com.paymentrecommendation.enums.PaymentInstrumentType;
import com.paymentrecommendation.models.Cart;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentFetcherContext;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.paymentrecommendation.enums.PaymentInstrumentFetcherType.CART_BASED;

// used decorator design pattern with both IS-A and HAS-A relationship
public class CartBasedInstrumentFetcher implements PaymentInstrumentFetcher {

    private PaymentInstrumentFetcher paymentInstrumentFetcher;
    private static Map<LineOfBusiness, Set<PaymentInstrumentType>> DIS_ALLOWED_BUSINESS_INSTRUMENT_TYPES = new EnumMap<>(LineOfBusiness.class);

    static {
        DIS_ALLOWED_BUSINESS_INSTRUMENT_TYPES.put(LineOfBusiness.COMMERCE, Collections.singleton(PaymentInstrumentType.NETBANKING));
        DIS_ALLOWED_BUSINESS_INSTRUMENT_TYPES.put(LineOfBusiness.INVESTMENT, Collections.singleton(PaymentInstrumentType.CREDIT_CARD));
        DIS_ALLOWED_BUSINESS_INSTRUMENT_TYPES.put(LineOfBusiness.CREDIT_CARD_BILL_PAYMENT, Collections.singleton(PaymentInstrumentType.CREDIT_CARD));
    }

    public CartBasedInstrumentFetcher(PaymentInstrumentFetcherLocator locator) {
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
        return CART_BASED;
    }

    @Override
    public List<PaymentInstrument> getInstruments(PaymentInstrumentFetcherContext context) {
        List<PaymentInstrument> instruments = getBasePaymentInstruments(context);
        Cart cart = context.getCart();
        if (instruments == null || cart == null || cart.getCartDetail() == null) {
            return Collections.emptyList();
        }
        LineOfBusiness lineOfBusiness = cart.getLineOfBusiness();
        instruments = getAllowedInstruments(lineOfBusiness, instruments);
        Double cartAmount = cart.getCartDetail().getCartAmount();
        return instruments.stream()
                .filter(instrument ->
                        isInstrumentSupportedForAmount(cartAmount,
                                instrument.getPaymentInstrumentType(), lineOfBusiness))
                .collect(Collectors.toList());
    }

    private boolean isInstrumentSupportedForAmount(Double cartAmount, PaymentInstrumentType paymentInstrumentType, LineOfBusiness lineOfBusiness) {
        if (cartAmount == null) {
            return false;
        }
        boolean isPossible = false;
        if (paymentInstrumentType == PaymentInstrumentType.UPI) {
            if (lineOfBusiness == LineOfBusiness.CREDIT_CARD_BILL_PAYMENT) {
                isPossible = cartAmount <= 2_00_000;
            } else {
                isPossible = cartAmount <= 1_00_000;
            }
        } else if (paymentInstrumentType == PaymentInstrumentType.CREDIT_CARD && lineOfBusiness == LineOfBusiness.COMMERCE) {
            isPossible = cartAmount <= 2_50_000;
        } else if (paymentInstrumentType == PaymentInstrumentType.DEBIT_CARD){
            if (lineOfBusiness == LineOfBusiness.CREDIT_CARD_BILL_PAYMENT || lineOfBusiness == LineOfBusiness.COMMERCE) {
                isPossible = cartAmount <= 2_00_000;
            } else {
                isPossible = cartAmount <= 1_50_000;
            }
        } else if (paymentInstrumentType == PaymentInstrumentType.NETBANKING) {
            if (lineOfBusiness == LineOfBusiness.CREDIT_CARD_BILL_PAYMENT) {
                isPossible = cartAmount <= 2_00_000;
            } else if (lineOfBusiness == LineOfBusiness.INVESTMENT) {
                isPossible = cartAmount <= 1_50_000;
            }
        }
        return isPossible;
    }

    private List<PaymentInstrument> getAllowedInstruments(LineOfBusiness lineOfBusiness, List<PaymentInstrument> instruments) {
        return instruments.stream()
                .filter(paymentInstrument ->
                        !DIS_ALLOWED_BUSINESS_INSTRUMENT_TYPES.getOrDefault(lineOfBusiness, Collections.emptySet())
                                .contains(paymentInstrument.getPaymentInstrumentType())).collect(Collectors.toList());
    }
}
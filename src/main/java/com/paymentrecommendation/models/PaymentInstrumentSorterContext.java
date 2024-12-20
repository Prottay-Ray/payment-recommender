package com.paymentrecommendation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInstrumentSorterContext {

    private User user;
    private Cart cart;
    private List<PaymentInstrument> fetchedPaymentInstrumentList;

    public UserContext getUserContext() {
        return user.getUserContext();
    }
}
package com.paymentrecommendation.service.paymentinstrumentsorter;

import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.PaymentInstrumentSorterContext;

import java.util.List;

public interface PaymentInstrumentSorter {

    List<PaymentInstrument> getSorted(PaymentInstrumentSorterContext context);

}
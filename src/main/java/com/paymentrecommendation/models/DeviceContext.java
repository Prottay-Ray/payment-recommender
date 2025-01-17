package com.paymentrecommendation.models;

public class DeviceContext {

    public static final DeviceContext EMPTY_CONTEXT = new DeviceContext(false);

    //There can be other attributes in device context. We are only maintaining upi enabled as of now.
    private boolean upiEnabled;

    public DeviceContext(boolean upiEnabled) {
        this.upiEnabled = upiEnabled;
    }

    public boolean isUpiEnabled() {
        return upiEnabled;
    }

    public void setUpiEnabled(boolean upiEnabled) {
        this.upiEnabled = upiEnabled;
    }
}

package com.paymentrecommendation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserContext {

    public static final UserContext EMPTY_CONTEXT = new UserContext(DeviceContext.EMPTY_CONTEXT);

    //There can be more attributes within the UserContext. We are only keeping the DeviceContext as of now.
    private DeviceContext deviceContext;

    public UserContext(DeviceContext deviceContext) {
        this.deviceContext = deviceContext;
    }

    public DeviceContext getDeviceContext() {
        return deviceContext;
    }

    public void setDeviceContext(DeviceContext deviceContext) {
        this.deviceContext = deviceContext;
    }
}

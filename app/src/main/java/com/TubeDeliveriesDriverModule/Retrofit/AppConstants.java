package com.TubeDeliveriesDriverModule.Retrofit;

import java.io.Serializable;

public interface AppConstants extends Serializable {
    String STEP_ONE ="stepone" ;
    String BRAND = "manufacturers";
    String STEP_TWO = "steptwo";
    String STEP_THREE = "stepthree";
    String REGISTER = "driver_register";
    String LOGIN = "driver_login";
    String FORGOT_PASSWORD = "reset-token";
    String DRIVER_DETAILS = "driver-details";
    String LOGOUT = "driver-logout";
    String DRIVER_UPDATE = "driver-update";
    String DRIVER_PASSWORD = "driver-password";
    String DRIVER_AVAILABILITY = "driver-available";
    String IN_PROGRESS = "in_progress_order";
    String HISTORY = "history";
    String NOTIFICATIONS = "driver_notifications";
    String ACCEPT_ORDER = "accept_order";
    String CHANGE_ORDER_STATUS = "order_status_change";
    String GET_MY_DETAILS = "json";
    String EXCHAGE_RATE = "latest";
    String GET_CURRENCY = "alpha/{code}";
    String GET_CURRENCY_SYMBOL = "currencymap.json";
    String TRANSACTIONS = "transcations";
}

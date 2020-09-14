package com.TubeDeliveriesDriverModule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.Response;

public class CommonModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("steponeResponse")
    @Expose
    private ResponseBean steponeResponse;
    @SerializedName("registerResponse")
    @Expose
    private ResponseBean registerResponse;
    @SerializedName("loginResponse")
    @Expose
    private ResponseBean loginResponse;
    @SerializedName("driverDetails")
    @Expose
    private ResponseBean driverDetails;
    @SerializedName("notificationList")
    @Expose
    private List<ResponseBean> notificationList;

    @SerializedName(value = "ongoing_order",alternate = "history")
    @Expose
    private List<ResponseBean> ongoing_order;

    @SerializedName("transcations")
    @Expose
    private List<ResponseBean> transcations;

    public List<ResponseBean> getTranscations() {
        return transcations;
    }

    public void setTranscations(List<ResponseBean> transcations) {
        this.transcations = transcations;
    }

    public List<ResponseBean> getOngoing_order() {
        return ongoing_order;
    }

    public void setOngoing_order(List<ResponseBean> ongoing_order) {
        this.ongoing_order = ongoing_order;
    }

    public List<ResponseBean> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<ResponseBean> notificationList) {
        this.notificationList = notificationList;
    }

    public ResponseBean getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(ResponseBean driverDetails) {
        this.driverDetails = driverDetails;
    }

    public ResponseBean getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(ResponseBean loginResponse) {
        this.loginResponse = loginResponse;
    }

    public ResponseBean getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(ResponseBean registerResponse) {
        this.registerResponse = registerResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseBean getSteponeResponse() {
        return steponeResponse;
    }

    public void setSteponeResponse(ResponseBean steponeResponse) {
        this.steponeResponse = steponeResponse;
    }
}

package com.TubeDeliveriesDriverModule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverBankDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("driver_id")
    @Expose
    private int driverId;
    @SerializedName("bank_type")
    @Expose
    private String bankType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("iban")
    @Expose
    private String iban;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}

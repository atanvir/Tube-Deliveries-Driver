package com.TubeDeliveriesDriverModule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverProfile {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("driver_id")
    @Expose
    private int driverId;
    @SerializedName("driver_image")
    @Expose
    private String driverImage;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("modal")
    @Expose
    private String modal;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("license_number")
    @Expose
    private String licenseNumber;
    @SerializedName("license_image")
    @Expose
    private String licenseImage;
    @SerializedName("insurance_number")
    @Expose
    private String insuranceNumber;
    @SerializedName("insurance_image")
    @Expose
    private String insuranceImage;
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

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseImage() {
        return licenseImage;
    }

    public void setLicenseImage(String licenseImage) {
        this.licenseImage = licenseImage;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getInsuranceImage() {
        return insuranceImage;
    }

    public void setInsuranceImage(String insuranceImage) {
        this.insuranceImage = insuranceImage;
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

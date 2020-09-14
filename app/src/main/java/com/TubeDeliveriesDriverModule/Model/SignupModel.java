package com.TubeDeliveriesDriverModule.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SignupModel implements Parcelable {
    //Step One
    private String first_name;
    private String last_name;
    private String email;
    private String country_code;
    private String phone_no;
    private String password;
    private String state;
    private String device_type;
    private String device_token;
    private Double latitude;
    private Double longitude;
    private String city;

    //Step 2
    private String driver_image;
    private String type;
    private String modal;
    private String vehicle_number;
    private List<String> vehicle_images;
    private String license_number;
    private String license_image;
    private String insurance_number;
    private String insurance_image;

    //step 3
    private String bank_type;
    private String name;
    private String account_number;
    private String iban;

    public SignupModel()
    {

    }


    protected SignupModel(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        country_code = in.readString();
        phone_no = in.readString();
        password = in.readString();
        state = in.readString();
        device_type = in.readString();
        device_token = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        city = in.readString();
        driver_image = in.readString();
        type = in.readString();
        modal = in.readString();
        vehicle_number = in.readString();
        vehicle_images = in.createStringArrayList();
        license_number = in.readString();
        license_image = in.readString();
        insurance_number = in.readString();
        insurance_image = in.readString();
        bank_type = in.readString();
        name = in.readString();
        account_number = in.readString();
        iban = in.readString();
    }

    public static final Creator<SignupModel> CREATOR = new Creator<SignupModel>() {
        @Override
        public SignupModel createFromParcel(Parcel in) {
            return new SignupModel(in);
        }

        @Override
        public SignupModel[] newArray(int size) {
            return new SignupModel[size];
        }
    };

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public List<String> getVehicle_images() {
        return vehicle_images;
    }

    public void setVehicle_images(List<String> vehicle_images) {
        this.vehicle_images = vehicle_images;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getLicense_image() {
        return license_image;
    }

    public void setLicense_image(String license_image) {
        this.license_image = license_image;
    }

    public String getInsurance_number() {
        return insurance_number;
    }

    public void setInsurance_number(String insurance_number) {
        this.insurance_number = insurance_number;
    }

    public String getInsurance_image() {
        return insurance_image;
    }

    public void setInsurance_image(String insurance_image) {
        this.insurance_image = insurance_image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(country_code);
        dest.writeString(phone_no);
        dest.writeString(password);
        dest.writeString(state);
        dest.writeString(device_type);
        dest.writeString(device_token);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(city);
        dest.writeString(driver_image);
        dest.writeString(type);
        dest.writeString(modal);
        dest.writeString(vehicle_number);
        dest.writeStringList(vehicle_images);
        dest.writeString(license_number);
        dest.writeString(license_image);
        dest.writeString(insurance_number);
        dest.writeString(insurance_image);
        dest.writeString(bank_type);
        dest.writeString(name);
        dest.writeString(account_number);
        dest.writeString(iban);
    }
}

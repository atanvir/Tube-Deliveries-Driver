package com.TubeDeliveriesDriverModule.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBean {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("auth")
    @Expose
    private Auth auth;
    @SerializedName("driver")
    @Expose
    private Driver driver;


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("google_id")
    @Expose
    private Object googleId;
    @SerializedName("apple_id")
    @Expose
    private Object appleId;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("address_type")
    @Expose
    private int addressType;
    @SerializedName("street")
    @Expose
    private Object street;
    @SerializedName("building")
    @Expose
    private Object building;
    @SerializedName("floor")
    @Expose
    private Object floor;
    @SerializedName("apartment")
    @Expose
    private Object apartment;
    @SerializedName("landmark")
    @Expose
    private Object landmark;
    @SerializedName("is_phone_verify")
    @Expose
    private int isPhoneVerify;
    @SerializedName("phone_otp")
    @Expose
    private Object phoneOtp;
    @SerializedName("userimage")
    @Expose
    private Object userimage;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("driver_profile")
    @Expose
    private DriverProfile driverProfile;
    @SerializedName("driver_bank_detail")
    @Expose
    private DriverBankDetail driverBankDetail;
    @SerializedName("vehicle_image")
    @Expose
    private List<VehicleImage> vehicleImage = null;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("type")
    @Expose
    private String type;

   @SerializedName("date")
   @Expose
   private String date;

   @SerializedName("acceptance")
   @Expose
   private String acceptance;

    @SerializedName("orderMenu")
    @Expose
    private List<OrderMenu> orderMenu = null;

    @SerializedName("seller")
    @Expose
    private Seller seller;

    @SerializedName("user")
    @Expose
    private Seller user;

    @SerializedName("payment_type")
    @Expose
    private String payment_type;

    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;

    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;

    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    @SerializedName("order_number")
    @Expose
    private String order_number;

    @SerializedName("expected_delivery_time")
    @Expose
    private String expected_delivery_time;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpected_delivery_time() {
        return expected_delivery_time;
    }

    public void setExpected_delivery_time(String expected_delivery_time) {
        this.expected_delivery_time = expected_delivery_time;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public List<OrderMenu> getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(List<OrderMenu> orderMenu) {
        this.orderMenu = orderMenu;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Seller getUser() {
        return user;
    }

    public void setUser(Seller user) {
        this.user = user;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public Object getAppleId() {
        return appleId;
    }

    public void setAppleId(Object appleId) {
        this.appleId = appleId;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getBuilding() {
        return building;
    }

    public void setBuilding(Object building) {
        this.building = building;
    }

    public Object getFloor() {
        return floor;
    }

    public void setFloor(Object floor) {
        this.floor = floor;
    }

    public Object getApartment() {
        return apartment;
    }

    public void setApartment(Object apartment) {
        this.apartment = apartment;
    }

    public Object getLandmark() {
        return landmark;
    }

    public void setLandmark(Object landmark) {
        this.landmark = landmark;
    }

    public int getIsPhoneVerify() {
        return isPhoneVerify;
    }

    public void setIsPhoneVerify(int isPhoneVerify) {
        this.isPhoneVerify = isPhoneVerify;
    }

    public Object getPhoneOtp() {
        return phoneOtp;
    }

    public void setPhoneOtp(Object phoneOtp) {
        this.phoneOtp = phoneOtp;
    }

    public Object getUserimage() {
        return userimage;
    }

    public void setUserimage(Object userimage) {
        this.userimage = userimage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public DriverProfile getDriverProfile() {
        return driverProfile;
    }

    public void setDriverProfile(DriverProfile driverProfile) {
        this.driverProfile = driverProfile;
    }

    public DriverBankDetail getDriverBankDetail() {
        return driverBankDetail;
    }

    public void setDriverBankDetail(DriverBankDetail driverBankDetail) {
        this.driverBankDetail = driverBankDetail;
    }

    public List<VehicleImage> getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(List<VehicleImage> vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}

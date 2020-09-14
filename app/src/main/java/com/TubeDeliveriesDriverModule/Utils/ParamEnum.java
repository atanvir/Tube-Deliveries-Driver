package com.TubeDeliveriesDriverModule.Utils;

/**
 * Created by Mahipal on 2/11/18.
 */
public enum ParamEnum {

    FIRST_NAME("First Name"),
    LAST_NAME("Last Name"),
    ADDRESS("Address"),
    SOCIAL_EMAIL("email"),
    EXTRA_NOTE("extra_note"),
    Success("true"),
    L_NAME("l_name"),
    LOGIN("login"),
    PRICE("price"),
    AMOUNT_TO_BE_PAID("amount_to_be_paid"),
    DISCOUNT("discount"),
    LOGOUT("logout"),
    LANDMARK("landmark"),
    ZERO("0"),
    ONE("1"),
    PINCODE("pincode"),
    ANDROID("android"),
    USER_CURRENT_ADDRESS("user_current_address"),
    TOP_RATED_RESTRO("Top Rated"),
    NEAR_BY_RESTRO("Near By"),
    ISCUSTOM_ADDRESS("is_custom_address"),
    DOWNLAODING(" downloading"),
    COUNTRY_CODE("Country Code"),
    FROM("from"),
    NAME("name"),
    ENGLISH("en"),
    DOWN_Assigments("Download Assignment"),
    SOL_Assigments("Solution to Assignment"),
    SOCIAL_ID("socialid"),
    PASS("Pass"),
    AUTHORIZATION("Authorization"),
    OFFSUPPOR_MAT("Offline Support Material"),
    IMAGE("image"),
    LOCALISATION("x-localisation"),
    EMAIL("Email"),
    PHONE("Phone Number"),
    F_NAME("f_name"),
    TYPE("type"),
    TITLE("TITLE"),
    LATITUDE("Latitude"),
    LONGITUDE("Longitude"),
    LIVE_START("livestart"),
    LIVE_LECTURE_SCHEDULE("livecreate"),
    RESTAURANT_NAME("restaurant_name"),
    RESTAURANT_ADDRESS("restaurant_address"),
    MSG("message"),
    MENU("Menu_Item"),
    MAIN_MENU_ITEM("main_menu_item"),
    CHangePass("Change Password"),
    AREA("area"),
    WEB_URL("URL"),
    RESTRO_ID("restro_id"),
    ORDER_ID("order_id"),
    HOUSE_NO("houseno"),
    GENDER("gender"),
    DISTANCE("distance"),
    CURRENT("Current"),
    CATEGORY_ID("category_id"),
    HOME("Home"),
    EAT_OPTION("EAT_OPTION"),
    OFFERED_SERVICE("offered_service"),
    CATEGORY_COMBO("category_combo"),
    ITEM_TYPE("item_type"),
    EAT_TYPE("eat_type"),
    NEAR_BY_GROCERY_STORE("near_by_grocery_store"),
    TASTE("taste"),
    STORE_TYPE("store_type");


    private final String value;

    ParamEnum(String value) {
        this.value=value;
    }

    ParamEnum(){
     this.value=null;
    }

    public String theValue() {
        return this.value;
    }


}
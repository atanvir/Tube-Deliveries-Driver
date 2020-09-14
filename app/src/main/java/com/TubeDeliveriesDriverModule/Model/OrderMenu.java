package com.TubeDeliveriesDriverModule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderMenu {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("item_type")
    @Expose
    private long itemType;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("quantity")
    @Expose
    private long quantity;
    @SerializedName("customizeItem")
    @Expose
    private String customizeItem;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getItemType() {
        return itemType;
    }

    public void setItemType(long itemType) {
        this.itemType = itemType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getCustomizeItem() {
        return customizeItem;
    }

    public void setCustomizeItem(String customizeItem) {
        this.customizeItem = customizeItem;
    }
}

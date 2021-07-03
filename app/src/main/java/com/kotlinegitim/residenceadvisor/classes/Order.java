package com.kotlinegitim.residenceadvisor.classes;

import java.util.ArrayList;

public class Order {
    private String ownerID;
    private String ownerNameSurname;
    private String date;
    private Integer productCount;
    private String productStatus;
    private ArrayList<Product> shopList;
    //private var shopList: ArrayList<Product> = ArrayList()

    public Order(String ownerID, String ownerNameSurname, String date, Integer productCount, String productStatus, ArrayList<Product> shopList) {
        this.ownerID = ownerID;
        this.ownerNameSurname = ownerNameSurname;
        this.date = date;
        this.productCount = productCount;
        this.productStatus = productStatus;
        this.shopList = new ArrayList<Product>();
    }

    public Order(){

    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public ArrayList<Product> getShopList() {
        return shopList;
    }

    public void setShopList(ArrayList<Product> shopList) {
        this.shopList = shopList;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getOwnerNameSurname() {
        return ownerNameSurname;
    }

    public void setOwnerNameSurname(String ownerNameSurname) {
        this.ownerNameSurname = ownerNameSurname;
    }
}

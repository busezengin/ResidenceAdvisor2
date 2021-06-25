package com.kotlinegitim.residenceadvisor.classes;

public class Product {
    private String name;
    private Integer price;
    private Integer count;
    private String type;

    public Product(String name, Integer price, Integer count, String type) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.type = type;
    }

    public Product(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

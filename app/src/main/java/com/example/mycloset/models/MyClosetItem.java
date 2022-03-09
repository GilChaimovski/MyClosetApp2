package com.example.mycloset.models;

/**
 * This class represents a single item on the shop
 */
public class MyClosetItem {
    private double price;
    private String name;
    private String category;
    private String image;

    public MyClosetItem(double price, String name, String category,String image) {
        this.price = price;
        this.name = name;
        this.category = category;
        this.image = image;
    }

    public MyClosetItem() {
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

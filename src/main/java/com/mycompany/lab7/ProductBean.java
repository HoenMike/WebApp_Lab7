package com.mycompany.lab7;

public class ProductBean {

    private int productID;
    private String name;
    private String manufacturer;
    private String country;
    private double price;

    public ProductBean(int productID, String name, String manufacturer, String country, double price) {
        this.productID = productID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.country = country;
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCountry() {
        return country;
    }

    public double getPrice() {
        return price;
    }
}

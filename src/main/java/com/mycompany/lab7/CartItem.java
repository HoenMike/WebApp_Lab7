package com.mycompany.lab7;

public class CartItem {

    private static int nextItemId = 1;
    private int itemId;
    private ProductBean product;
    private int quantity;

    public CartItem(ProductBean product, int quantity) {
        this.itemId = nextItemId++;
        this.product = product;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public ProductBean getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

import java.util.List;

/**
 *
 * @author HP
 */
public class product {
    private int productID;
    private Store store;
    private String productName;
    private float price;
    private int stockLevel;
    private String category;

    public product(int productID, Store store, String productName, float price, int stockLevel, String category) {
        this.productID = productID;
        this.store = store;
        this.productName = productName;
        this.price = price;
        this.stockLevel = stockLevel;
        this.category = category;
    }

    
    public product(int productID,  String productName, String category, int stockLevel, float price) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockLevel = stockLevel;
        this.category = category;
    }
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
    
}

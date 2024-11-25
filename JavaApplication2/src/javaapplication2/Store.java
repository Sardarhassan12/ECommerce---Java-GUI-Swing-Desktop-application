/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author HP
 */
public class Store {
    
    //attributes
    private int storeID;
    private String storeName;
    private StoreOwner storeOwner;
//    private ArrayList<Product> productList;
    private String storeCategory;
    private String storeAddress;
    private long storePhone;

    //constructor
    public Store(int storeID, String storeName, StoreOwner storeOwner, String storeCategory, String storeAddress, long storePhone) {
        this.storeID= storeID;
        this.storeName =storeName;
        this.storeOwner = storeOwner;
        this.storeCategory = storeCategory;
        this.storeAddress = storeAddress;
        this.storePhone = storePhone;
//        this.productList = new ArrayList<>();
    }

    
    //Functions
    public void addProduct(Connection connection,String productName,String productCategory, int storeId, int productStock, float productPrice){
        String query = "insert into product (productName, category, stockLevel, price, storeId) values ('"+productName+"','"+productCategory+"' ,'"+productStock+"' ,'"+productPrice+"' ,'"+storeId+"')";
                
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            if(result>0){
                JOptionPane.showMessageDialog(null, "Product added successfully!");
            }
            else{
                JOptionPane.showMessageDialog(null, "Product Not added !");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public product getProduct(Connection connection, int productId) {
        String query = "SELECT * FROM product WHERE productId = '" + productId + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                
                int storeId = result.getInt("storeId");
                String productName = result.getString("productName");
                String productCategory = result.getString("category");
                int productStock = result.getInt("stockLevel");
                float productPrice = result.getFloat("price");

                return new product(productId, this, productName,productPrice, productStock, productCategory);
            } else {
                JOptionPane.showMessageDialog(null, "Product not found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "An error occurred while fetching product details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
     
    public void updateProduct(Connection connection,String productName,String productCategory, int productId, int productStock, float productPrice){
        String query = "Update product set productName = '"+productName+"', category = '"+productCategory+"', stockLevel='"+productStock+"', price='"+productPrice+"' where productId = '"+productId+"'";
                
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            if(result>0){
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
            }
            else{
                JOptionPane.showMessageDialog(null, "Product Not added !");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Getter & Setter
    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public StoreOwner getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(StoreOwner storeOwner) {
        this.storeOwner = storeOwner;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public long getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(long storePhone) {
        this.storePhone = storePhone;
    }
    
}

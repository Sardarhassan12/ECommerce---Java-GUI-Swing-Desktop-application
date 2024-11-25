/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

//import static com.mycompany.e.commerce_platform.ECommerce_Platform.input;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author HP
 */
public class StoreOwner extends Person{
    
    private ArrayList<Store> stores = new ArrayList<>();       //tocheck******************
    
    //constructor
    public StoreOwner(String name, String email, String userName, String password, String userRole) {
    
        super(name, email, userName, password, userRole);
    
    }
    
    //functions
    public void addNewStore(Connection connection, String storeName,String storeOwnerUserName,String storeCategory, String storePayment,String storeShipping,String storeEmail,long storePhone,String storeAddress ){
        
        String query = "insert into stores (storeName, storeOwnerUserName, storeCategory, storePayment, storeShipping, storeEmail, storePhone, storeAddress) values ('"+storeName+"','"+storeOwnerUserName+"' ,'"+storeCategory+"' ,'"+storePayment+"' ,'"+storeShipping+"','"+storeEmail+"' ,'"+storePhone+"' ,'"+storeAddress+"')";
                
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Store added successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void viewAllStore(Connection connection, StoreOwner storeOwner, JPanel ExistingStorePanel, GUI guiInstance){
        ExistingStorePanel.removeAll(); 
        String query = "Select * from stores where storeOwnerUserName = '"+this.getUserName()+"'";
        
        stores.clear(); 
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                Store existingStores = new Store(Integer.parseInt(result.getString("storeId")),result.getString("storeName"),storeOwner , result.getString("storeCategory"), result.getString("storeAddress"), result.getLong("storePhone"));
                stores.add(existingStores);
                System.out.println("View All Stores Successfully Run.........");
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (stores != null && !stores.isEmpty()) {
                    
                    ExistingStorePanel.setLayout(new GridLayout(0, 2, 10, 10));
                    guiInstance.ProductStoreBoxField.removeAllItems();
                    for (Store store : stores) {
                        guiInstance.ProductStoreBoxField.addItem(store.getStoreID() + " -- " + store.getStoreName());

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBackground(Color.WHITE);
                        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2)); 
                        panel.setPreferredSize(new Dimension(300, 300)); 
                        panel.setMaximumSize(new Dimension(300, 300));
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.setBackground(new Color(255, 255, 255)); 

                        
                        JLabel titleLabel = new JLabel(store.getStoreName());
                        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        titleLabel.setForeground(new Color(33, 104, 186)); 
                        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                        JPanel detailsPanel = new JPanel();
                        detailsPanel.setLayout(new GridLayout(4, 2, 10, 10)); 
                        detailsPanel.setBackground(Color.WHITE);

                        detailsPanel.add(new JLabel("Owner Name:"));
                        detailsPanel.add(new JLabel(storeOwner.getName()));
                        detailsPanel.add(new JLabel("Category:"));
                        detailsPanel.add(new JLabel(store.getStoreCategory()));
                        detailsPanel.add(new JLabel("Phone:"));
                        detailsPanel.add(new JLabel(String.valueOf(store.getStorePhone())));
                        detailsPanel.add(new JLabel("Address:"));
                        detailsPanel.add(new JLabel(store.getStoreAddress()));

                        
                        panel.add(titleLabel);
                        panel.add(Box.createVerticalStrut(40)); 
                        panel.add(detailsPanel);

                        
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.setPreferredSize(new Dimension(100, 50));
                        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

                        // Update button
                        JButton updateBtn = new JButton("Update");
                        updateBtn.setPreferredSize(new Dimension(200, 30)); 
                        updateBtn.setBackground(new Color(0, 120, 215));
                        updateBtn.setForeground(Color.WHITE); 
                        updateBtn.setFocusPainted(false);
                        updateBtn.setToolTipText("Update Store Details");
                        updateBtn.addActionListener(e -> updateStore(store.getStoreID(), connection, guiInstance));

                        // Delete button
                        JButton deleteBtn = new JButton("Delete");
                        deleteBtn.setPreferredSize(new Dimension(200, 30)); 
                        deleteBtn.setBackground(new Color(255, 69, 58));
                        deleteBtn.setForeground(Color.WHITE);
                        deleteBtn.setFocusPainted(false);
                        deleteBtn.setToolTipText("Delete Store");
                        deleteBtn.addActionListener(e -> deleteStore(store.getStoreID(), connection));

                        
                        buttonPanel.add(updateBtn);
                        buttonPanel.add(deleteBtn);

                        
                        panel.add(buttonPanel);
                        ExistingStorePanel.add(panel); 
                    }

                    
                    ExistingStorePanel.revalidate();
                    ExistingStorePanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "No stores found for this owner.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
    }
    
    public void updateStore(int storeId, Connection connection,GUI guiInstance){
        String query = "select * from stores where storeId = '"+storeId+"' and storeOwnerUserName = '"+this.getUserName()+"'";
         
        try {
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next()){
               
                guiInstance.Dashboard_Panel.removeAll();
                guiInstance.Dashboard_Panel.add(guiInstance.Update_Store_Panel1);
                guiInstance.repaint();
                guiInstance.revalidate();
                
                
                String storeName = result.getString("storeName");
                String ownerName = result.getString("storeOwnerUserName");
                String storeCategory = result.getString("storeCategory");
                String storePhone = result.getString("storePhone");
                String storeAddress = result.getString("storeAddress");
                String storeEmail = result.getString("storeEmail");
                String paymentMethod = result.getString("storePayment");
                String shippingBox = result.getString("storeShipping");

                guiInstance.UpdateStoreIdField.setText(String.valueOf(storeId));
                guiInstance.UpdateStoreNameField1.setText(storeName);
                guiInstance.UpdateOwnerField1.setText(ownerName);
                guiInstance.UpdatePhoneField1.setText(storePhone);
                guiInstance.UpdateAddressField1.setText(storeAddress);
                guiInstance.UpdateEmailField1.setText(storeEmail);
                guiInstance.UpdatePaymentBoxField1.setSelectedItem(paymentMethod);
                guiInstance.UpdateCategoryBoxField1.setSelectedItem(storeCategory); 
                guiInstance.UpdateShippingBoxField1.setSelectedItem(shippingBox); 
                
                
            }
            else{
                JOptionPane.showMessageDialog(null, "Some Error Occur", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
        
            Logger.getLogger(StoreOwner.class.getName()).log(Level.SEVERE, null, ex);
        
        }
        
        
    }
    public void deleteStore(int storeId, Connection connection){
        
    }
    
    public void displayAllProducts(Connection connection, JTable productTable) {
        String query = "SELECT * FROM product";
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0); 
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int val = Integer.parseInt(result.getString("storeId"));
                model.addRow(new Object[]{
                    result.getString("productId"),
                    result.getString("productName"),
                    result.getString("category"),
                    result.getDouble("price"),
                    result.getInt("stockLevel"),
                    this.getStoresById(val).getStoreName()
                });
            }
        } catch (SQLException ex) {
        }
    }

    public Store getStoresById(int storeId) {
        if (stores != null && !stores.isEmpty()) {
                  
            for (Store store : stores) {
                if(store.getStoreID() == storeId){
                    return store;
                }
            }
        }
        return null;
    }
    
    
    public Store getStoreByProductId(Connection connection, int productId) {
        String query = "SELECT storeId FROM product WHERE productId = " + productId;
        Store store = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                int storeId = result.getInt("storeId");

                store = this.getStoresById(storeId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }

        return store; 
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
    
    
}

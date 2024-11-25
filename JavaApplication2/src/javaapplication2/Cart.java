/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;


import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author Hp
 */
public class Cart {


    private int cartId;
    private String userName;
    private double totalValue;
    private ArrayList<product> products;  
    
    
    public Cart(String userName, Connection connection) {
        this.userName = userName;
        this.totalValue = 0.00;
        this.products = new ArrayList<>(); 
        this.cartId = createCart(connection);  
    }

    private int createCart(Connection connection) {
        String cartQuery = "INSERT INTO cart (userName, total_value) VALUES ('" + this.userName + "', 0.00)";
        try {
            Statement statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(cartQuery, Statement.RETURN_GENERATED_KEYS);

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

   public void addToCart(int productId, int quantity, double price, Connection connection) {
        try {

            String productCheckQuery = "SELECT COUNT(*) FROM product WHERE productId = " + productId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(productCheckQuery);

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                
                String cartItemQuery = "INSERT INTO cart_item (cartId, productId, quantity) VALUES (" + this.cartId + ", " + productId + ", " + quantity + ")";
                statement.executeUpdate(cartItemQuery);
                updateTotalValue(price, quantity,productId, connection);
            } else {
                
                JOptionPane.showMessageDialog(null, "Product not found in the database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void updateTotalValue(double price, int quantity, int productId, Connection connection) {
        double itemTotalPrice = price * quantity;
        double currentCartTotal = 0.0;

        String fetchCartTotalQuery = "SELECT total_value FROM cart WHERE cart_id = " + this.cartId;

        String cartUpdateQuery = "UPDATE cart SET total_value = ? WHERE cart_id = ?";

        String updateStockQuery = "UPDATE product SET stockLevel = stockLevel - ? WHERE productId = ?";

        try {
            
            PreparedStatement fetchStatement = connection.prepareStatement(fetchCartTotalQuery);
            ResultSet resultSet = fetchStatement.executeQuery();
            if (resultSet.next()) {
                currentCartTotal = resultSet.getDouble("total_value");
            }

            double updatedCartTotal = currentCartTotal + itemTotalPrice;

            PreparedStatement updateCartStatement = connection.prepareStatement(cartUpdateQuery);
            updateCartStatement.setDouble(1, updatedCartTotal);
            updateCartStatement.setInt(2, this.cartId);
            updateCartStatement.executeUpdate();

            PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery);
            updateStockStatement.setInt(1, quantity);
            updateStockStatement.setInt(2, productId);
            updateStockStatement.executeUpdate();

            this.totalValue = updatedCartTotal;

        } catch (SQLException ex) {
            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public ArrayList<String[]> getCartDetails(Connection connection) {
        String cartDetailsQuery =
            "SELECT c.total_value, ci.quantity, p.productName, p.price,p.productId " +
            "FROM cart c " +
            "JOIN cart_item ci ON c.cart_id = ci.cartId " +
            "JOIN product p ON ci.productId = p.productId " +
            "WHERE c.userName = '" + this.userName + "'";

        ArrayList<String[]> cartItems = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(cartDetailsQuery);

            while (resultSet.next()) {
                String[] item = new String[5];
                item[0] = resultSet.getString("productId"); 
                item[1] = resultSet.getString("productName"); 
                item[2] = String.valueOf(resultSet.getInt("quantity")); 
                item[3] = String.valueOf(resultSet.getDouble("price"));
                item[4] = String.valueOf(resultSet.getInt("quantity") * resultSet.getDouble("price"));
                cartItems.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cartItems;
    }


}

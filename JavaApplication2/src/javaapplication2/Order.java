/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

import java.awt.BorderLayout;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hp
 */
public class Order {

    private int orderId;
    private String userName;
    private String customerAddress;
    private double totalPayment;
    private Timestamp orderDate;
    private String paymentType;

    public Order(String userName, String customerAddress, String paymentType,double totalPayment) {
        this.userName = userName;
        this.customerAddress = customerAddress;
        this.totalPayment = totalPayment;
        this.paymentType = paymentType;
    }

    public Order(int orderId, String userName, String customerAddress, double totalPayment, Timestamp orderDate) {
        this.orderId = orderId;
        this.userName = userName;
        this.customerAddress = customerAddress;
        this.totalPayment = totalPayment;
        this.orderDate = orderDate;
    }

    public boolean addOrder(Connection connection, ArrayList<Integer> productIds, ArrayList<Integer> quantities) {
        String orderInsertQuery = "INSERT INTO orders (userName, payment_type, customer_address, total_payment) VALUES (?, ?, ?, ?)";
        String orderItemsInsertQuery = "INSERT INTO order_items (order_id, productId, quantity) VALUES (?, ?, ?)";
        String deleteCartItemsQuery = "DELETE FROM cart_item WHERE cartId IN (SELECT cart_id FROM cart WHERE userName = ?)";
        String updateCartTotalQuery = "UPDATE cart SET total_value = 0 WHERE userName = ?";

        try (
            PreparedStatement orderStatement = connection.prepareStatement(orderInsertQuery, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement orderItemsStatement = connection.prepareStatement(orderItemsInsertQuery);
            PreparedStatement deleteCartItemsStatement = connection.prepareStatement(deleteCartItemsQuery);
            PreparedStatement updateCartTotalStatement = connection.prepareStatement(updateCartTotalQuery)
        ) {
        
            orderStatement.setString(1, this.userName);
            orderStatement.setString(2, this.paymentType);
            orderStatement.setString(3, this.customerAddress);
            orderStatement.setDouble(4, this.totalPayment);
            orderStatement.executeUpdate();

            ResultSet generatedKeys = orderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.orderId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Order creation failed, no ID obtained.");
            }

            for (int i = 0; i < productIds.size(); i++) {
                orderItemsStatement.setInt(1, this.orderId);
                orderItemsStatement.setInt(2, productIds.get(i));
                orderItemsStatement.setInt(3, quantities.get(i));
                orderItemsStatement.executeUpdate();
            }

            deleteCartItemsStatement.setString(1, this.userName);
            deleteCartItemsStatement.executeUpdate();

            updateCartTotalStatement.setString(1, this.userName);
            updateCartTotalStatement.executeUpdate();

            return true;

        } catch (SQLException ex) {
            return false;
        }
    }


    public static ArrayList<Order> getStoreOwnerOrders(Connection connection, int storeOwnerId) {
        String query = "SELECT o.* FROM orders o " +
                       "JOIN order_items oi ON o.order_id = oi.order_id " +
                       "JOIN product p ON oi.productId = p.productId " +
                       "WHERE p.storeOwnerId = ?";
        ArrayList<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, storeOwnerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getString("userName"),
                        resultSet.getString("customer_address"),
                        resultSet.getDouble("total_payment"),
                        resultSet.getTimestamp("order_date")
                );
                orders.add(order);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return orders;
    }
    
  public static void getCustomerOrders(Connection connection, JPanel HomeOrderPanel, String userName) {
     String query = "SELECT * FROM orders WHERE userName = '" + userName + "'"; // Simple query
        
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            
            // Create a JTable to display the orders
            String[] columnNames = {"Order ID", "User Name", "Address", "Total Payment", "Order Date"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            // Fetch the orders and add them to the table model
            boolean hasData = false; // To check if there are any orders
            while (resultSet.next()) {
                hasData = true;
                Object[] rowData = {
                        resultSet.getInt("order_id"),
                        resultSet.getString("userName"),
                        resultSet.getString("customer_address"),
                        resultSet.getDouble("total_payment"),
                        resultSet.getTimestamp("order_date")
                };
                tableModel.addRow(rowData);
            }

            // If there are no orders, show a message
            if (!hasData) {
                JLabel noOrdersLabel = new JLabel("No Orders Found!");
                HomeOrderPanel.removeAll();  // Remove previous components
                HomeOrderPanel.add(noOrdersLabel);  // Add the label to notify the user
                HomeOrderPanel.repaint();
                HomeOrderPanel.revalidate();
                return;
            }

            // Create a JTable with the table model
            JTable table = new JTable(tableModel);
            table.setFillsViewportHeight(true); // Ensure the table fills the viewport

            JScrollPane scrollPane = new JScrollPane(table);  // Wrap the table in a JScrollPane

            // Clear previous components and add the updated table to the panel
            HomeOrderPanel.removeAll(); // Remove existing components
            HomeOrderPanel.setLayout(new BorderLayout()); // Use BorderLayout for proper resizing
            HomeOrderPanel.add(scrollPane, BorderLayout.CENTER); // Add JScrollPane containing the JTable
            HomeOrderPanel.repaint();
            HomeOrderPanel.revalidate(); // Revalidate the panel to show the new components

            System.out.println("Orders table updated successfully."); // Debugging message

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
}

}


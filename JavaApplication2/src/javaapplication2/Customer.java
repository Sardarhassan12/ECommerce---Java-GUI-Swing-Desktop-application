package javaapplication2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer extends Person {
    
//    private Cart cart;
//    private OrderList orderList;

    public Customer(String name, String email, String userName, String password, String userRole) {
    
        super(name, email, userName, password, userRole);
    
    }
   public void viewAllProducts(Connection connection, JPanel viewProductPanel) {
        viewProductPanel.removeAll(); 
        viewProductPanel.setPreferredSize(new Dimension(1080,778));
        viewProductPanel.setLayout(new BorderLayout());


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        mainPanel.setBackground(Color.WHITE); 

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        viewProductPanel.add(scrollPane, BorderLayout.CENTER);

        ArrayList<product> products = getProductsFromDatabase(connection);
        for (product product : products) {
            mainPanel.add(createProductCard(product,connection));
        }

        viewProductPanel.revalidate();
        viewProductPanel.repaint();
    }
   
    private ArrayList<product> getProductsFromDatabase(Connection connection) {
        
        ArrayList<product> products = new ArrayList<>();
        String query ="SELECT * FROM product";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productName = resultSet.getString("productName");
                String category = resultSet.getString("category");
                int stockLevel = resultSet.getInt("stockLevel");
                int productId = resultSet.getInt("productId");
                float price = resultSet.getFloat("price");
//                int storeId = resultSet.getInt("storeId");

                products.add(new product(productId,productName, category, stockLevel, price));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    
    }
    
    private JPanel createProductCard(product product,Connection connection) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.decode("#f4f4f4"));
        card.setPreferredSize(new Dimension(250, 350)); // Increased height for the counter

        JLabel nameLabel = new JLabel(product.getProductName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel categoryLabel = new JLabel("Category: " + product.getCategory());
        JLabel stockLabel = new JLabel("Stock: " + product.getStockLevel());
        JLabel priceLabel = new JLabel("Price: $" + product.getPrice());

        JPanel detailsPanel = new JPanel(new GridLayout(3, 1));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detailsPanel.add(categoryLabel);
        detailsPanel.add(stockLabel);
        detailsPanel.add(priceLabel);

        // Counter Panel
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        counterPanel.setBackground(Color.WHITE);

        JButton decrementButton = new JButton("-");
        decrementButton.setFocusPainted(false);

        JLabel counterLabel = new JLabel("1", JLabel.CENTER);
        counterLabel.setPreferredSize(new Dimension(30, 30));
        counterLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton incrementButton = new JButton("+");
        incrementButton.setFocusPainted(false);

        
        incrementButton.addActionListener(e -> {
            int currentCount = Integer.parseInt(counterLabel.getText());
            if (currentCount < product.getStockLevel()) { 
                counterLabel.setText(String.valueOf(currentCount + 1));
            } else {
                JOptionPane.showMessageDialog(
                    card, 
                    "Cannot exceed available stock level!", 
                    "Stock Limit", 
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        decrementButton.addActionListener(e -> {
            int currentCount = Integer.parseInt(counterLabel.getText());
            if (currentCount > 1) {
                counterLabel.setText(String.valueOf(currentCount - 1));
            }
        });

        counterPanel.add(decrementButton);
        counterPanel.add(counterLabel);
        counterPanel.add(incrementButton);

       
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(Color.decode("#007bff"));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        
        addToCartButton.addActionListener(e -> {
            int quantity = Integer.parseInt(counterLabel.getText());
            addToCart(product, quantity, connection);
        });
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(counterPanel, BorderLayout.NORTH);
        buttonPanel.add(addToCartButton, BorderLayout.SOUTH);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    public void addToCart(product product, int quantity, Connection connection) {
        Cart cart = new Cart(this.getUserName(), connection);
        cart.addToCart(product.getProductID(), quantity, product.getPrice(), connection);
        JOptionPane.showMessageDialog(
            null, 
            "Added " + quantity + " " + product.getProductName() + " to the cart.", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void getCartDetailss(Connection connection, JLabel totalCartItem, JLabel totalCartValue, JTable cartItemTable) {
        System.out.println("Pass7");
        Cart cart = new Cart(this.getUserName(), connection);
        System.out.println("Pass8");
        ArrayList<String[]> cartItems = cart.getCartDetails(connection);
        System.out.println("Pass9");
        
        double totalValue = 0;
        int totalItems = 0;
        for (String[] item : cartItems) {
            totalItems += Integer.parseInt(item[2]);
            totalValue += Double.parseDouble(item[4]); 
        }

        totalCartItem.setText(" "+totalItems);
        totalCartValue.setText("Rs-/ "+totalValue);

        String[] columnNames = {"Product Id","Product Name", "Quantity", "Price", "Total"};
        String[][] tableData = cartItems.toArray(new String[0][5]);

        cartItemTable.setModel(new javax.swing.table.DefaultTableModel(
            tableData,
            columnNames
        ));
    }

}
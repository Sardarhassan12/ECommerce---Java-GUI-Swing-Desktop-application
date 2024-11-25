/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Person {
    
    //attributes
    private String userName;
    private String password;
    private String name;
    private String email;
    private String userRole;
    
    //constructor
    
    Person(String username, String pass){
        this.userName = username;
        this.password = pass;
        // this constructor is used for db fucntionality.
    }
    
    Person(String name, String email, String userName, String password, String userRole){
        
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        
    }
    
    
    //Logging in user and based on role making object of store owner or customer
    public Person Login(Connection connection){
        String query  = "Select * from users where userName = '"+userName+"' and password = '"+password+"'";
            
            try {
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(query);
                
                if(result.next()){
                    String role = result.getString("userRole");
                    
                    if(role.equals("Store Owner")){
                       
                        StoreOwner storeOwner = new StoreOwner(result.getString("name"), result.getString("email"), result.getString("userName"), result.getString("password"), result.getString("userRole"));
                        return storeOwner;
                        
                    }
                    
                    else if(role.equals("Customer")){
                        System.out.println("Pass1");
                        Customer customer = new Customer(result.getString("name"), result.getString("email"), result.getString("userName"), result.getString("password"), result.getString("userRole"));
                        return customer;
                    }
                    else{
                        System.out.println("User Not Found");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        return null;
    }
    
    //Registring User 
    public void Register(Connection connection, String repass) {
    if (password.equals(repass)) {
        
        String checkQuery = "SELECT COUNT(*) FROM users WHERE userName = '" + userName + "'";
        
        try {
            Statement checkStatement = connection.createStatement();
            ResultSet resultSet = checkStatement.executeQuery(checkQuery);

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                
                JOptionPane.showMessageDialog(null, "Username already taken, please choose another one.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                
                String insertQuery = "INSERT INTO users (userName, password, email, name, userRole) " +
                                     "VALUES ('" + userName + "', '" + password + "', '" + email + "', '" + name + "', '" + userRole + "')";
                Statement insertStatement = connection.createStatement();
                insertStatement.executeUpdate(insertQuery);
                
                if(userRole.equals("Customer")){
                    // here the cart class is being called and its fun creatte cart
                    String cartQuery = "INSERT INTO cart (userName)VALUES ('" + userName + "')";
                    Statement insertStatement1 = connection.createStatement();
                    insertStatement1.executeUpdate(cartQuery);
                }
                
                JOptionPane.showMessageDialog(null, "User registered successfully!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error during registration.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Password mismatch. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    //getter & setter
    public void setUserRole(String userRole) {    
        this.userRole = userRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}

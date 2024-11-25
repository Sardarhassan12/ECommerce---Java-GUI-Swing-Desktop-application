/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

//import com.sun.jdi.connect.spi.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class DatabaseConnection {
    private static Connection connection;
    
    public static Connection getConn(){
        String URL = "jdbc:mysql://localhost:3307/ecommerce";
        String username = "root";
        String password = "admin";
        if(connection == null){
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, username, password );
                System.out.println("************Connection Establish************");

            } catch (SQLException ex) {
                System.out.println("!!!!!!!!!!!!Connection Not Establish!!!!!!!!!!!!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
}

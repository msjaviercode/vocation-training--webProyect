/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boomeraling
 */
public class Conector {

    public Connection con = null;
    private PreparedStatement stm = null;
    private String address = "localhost/kupoi";
    private String usuario = "root";
    private String password = "";

    
    
    
    /**
     * 
     */
    public Conector() {

        if (con == null) {
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
                }
                con = DriverManager.getConnection("jdbc:mysql://" + address, usuario, password);
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Conector(String adress, String usuario, String password) {
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + adress, usuario, password);
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

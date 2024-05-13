/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aviakassaproject;

import DbConnections.RepositoryContext;
import com.sun.jdi.connect.spi.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class AviakassaProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        LoginPage loginpage = new LoginPage();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setTitle("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(loginpage);
        frame.setVisible(true);
    }

}

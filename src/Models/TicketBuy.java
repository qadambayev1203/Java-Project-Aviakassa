/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class TicketBuy {
    public int id;
    public String firstName;
    public String LastName;
    public String fatherName;
    public String passportNumber;
    public int ticket_id;
    public TicketModel ticket_;
}

//
//-- TicketBuy jadvalini yaratish
//CREATE TABLE TicketBuy (
//    id SERIAL PRIMARY KEY,
//    first_name VARCHAR(100),
//    last_name VARCHAR(100),
//    father_name VARCHAR(100),
//    passport_number VARCHAR(20),
//    ticket_id INT,
//    FOREIGN KEY (ticket_id) REFERENCES TicketModel(id)
//);
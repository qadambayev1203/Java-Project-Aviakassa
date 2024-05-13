/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class TicketModel {
    public int id;
    public String where_from;
    public String where_to;
    public String when;
    public String ticket_type;
    public int flight_number;
    public String time;
    public int seat_number;
 
}

//-- TicketModel jadvalini yaratish
//CREATE TABLE ticketmodel (
//    id SERIAL PRIMARY KEY,
//    where_from VARCHAR(255),
//    where_to VARCHAR(255),
//    when_date DATE,
//    ticket_type VARCHAR(50),
//    flight_number INT,
//    time_of_ TIME,
//    seat_number INT
//);

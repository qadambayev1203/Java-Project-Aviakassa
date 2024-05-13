/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DbConnections;

import Models.TicketBuy;
import Models.TicketModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class RepositoryContext {

    public Connection connection = null;
    private static final String URL = "jdbc:postgresql://localhost:5432/JavaProjectDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1203";

    ArrayList<TicketBuy> context_ = new ArrayList<>();

    public RepositoryContext() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Ma'lumotlar bazasiga muvaffaqiyatli ulanish!");
            } else {
                System.out.println("Ma'lumotlar bazasiga ulanishda xatolik!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Draiver topilmadi!");
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    // TicketModel obyektlarini o'qish uchun metod
    public ArrayList<TicketModel> getAllTickets() {
        ArrayList<TicketModel> tickets = new ArrayList<>();
        try {
            String query = "SELECT * FROM ticketmodel";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TicketModel ticket = new TicketModel();
                ticket.id = resultSet.getInt("id");
                ticket.where_from = resultSet.getString("where_from");
                ticket.where_to = resultSet.getString("where_to");
                ticket.when = resultSet.getString("when_date");
                ticket.ticket_type = resultSet.getString("ticket_type");
                ticket.flight_number = resultSet.getInt("flight_number");
                ticket.time = resultSet.getString("time_of");
                ticket.seat_number = resultSet.getInt("seat_number");
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }
        return tickets;
    }

    // TicketBuy obyektlarini o'qish uchun metod
    public ArrayList<TicketBuy> GetTicketAdmin() {
        ArrayList<TicketBuy> ticketBuys = new ArrayList<>();
        try {
            String query = "SELECT * FROM ticketbuy";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TicketBuy ticketBuy = new TicketBuy();
                ticketBuy.id = resultSet.getInt("id");
                ticketBuy.firstName = resultSet.getString("first_name");
                ticketBuy.LastName = resultSet.getString("last_name");
                ticketBuy.fatherName = resultSet.getString("father_name");
                ticketBuy.passportNumber = resultSet.getString("passport_number");
                ticketBuy.ticket_id = resultSet.getInt("ticket_id");

                // TicketModel obyektini o'qib olamiz
                int ticketId = ticketBuy.ticket_id;
                TicketModel ticket = getTicketById(ticketId);
                ticketBuy.ticket_ = ticket;

                ticketBuys.add(ticketBuy);
            }
        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }

        return ticketBuys;
    }

    public TicketBuy GetById(int id) {
        TicketBuy ticketBuy = new TicketBuy();
        try {
            String query = "SELECT * FROM ticketbuy WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ticketBuy.id = resultSet.getInt("id");
                ticketBuy.firstName = resultSet.getString("first_name");
                ticketBuy.LastName = resultSet.getString("last_name");
                ticketBuy.fatherName = resultSet.getString("father_name");
                ticketBuy.passportNumber = resultSet.getString("passport_number");
                ticketBuy.ticket_id = resultSet.getInt("ticket_id");
            }
            return ticketBuy;

        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }

        return ticketBuy;
    }

    // TicketModel obyektini bilet ID bo'yicha o'qish uchun maxsus metod
    private TicketModel getTicketById(int ticketId) {
        TicketModel ticket = null;
        try {
            String query = "SELECT * FROM ticketmodel WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ticketId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ticket = new TicketModel();
                ticket.id = resultSet.getInt("id");
                ticket.where_from = resultSet.getString("where_from");
                ticket.where_to = resultSet.getString("where_to");
                ticket.when = resultSet.getString("when_date");
                ticket.ticket_type = resultSet.getString("ticket_type");
                ticket.flight_number = resultSet.getInt("flight_number");
                ticket.time = resultSet.getString("time_of");
                ticket.seat_number = resultSet.getInt("seat_number");
            }
        } catch (SQLException e) {
            System.out.println("Bilet ID bo'yicha ma'lumotni o'qishda xatolik yuz berdi: " + e.getMessage());
        }
        return ticket;
    }

    public ArrayList<TicketBuy> GetTicketAdmin(TicketBuy model) throws ParseException {
        ArrayList<TicketBuy> ticketBuys = new ArrayList<>();
        try {

            String dateString = model.ticket_.when;
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsed = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            String query = "SELECT * FROM ticketbuy  \n"
                    + "INNER JOIN ticketmodel ON ticketmodel.id=ticketbuy.ticket_id\n"
                    + " WHERE ticketmodel.where_from=? and ticketmodel.where_to=? "
                    + " and ticketmodel.when_date=?\n"
                    + " and ticketmodel.ticket_type=? and ticketbuy.passport_number is null";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, model.ticket_.where_from);
            statement.setString(2, model.ticket_.where_to);
            statement.setDate(3, sqlDate);
            statement.setString(4, model.ticket_.ticket_type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TicketBuy ticketBuy = new TicketBuy();
                ticketBuy.id = resultSet.getInt("id");
                ticketBuy.firstName = resultSet.getString("first_name");
                ticketBuy.LastName = resultSet.getString("last_name");
                ticketBuy.fatherName = resultSet.getString("father_name");
                ticketBuy.passportNumber = resultSet.getString("passport_number");
                ticketBuy.ticket_id = resultSet.getInt("ticket_id");

                // TicketModel obyektini o'qib olamiz
                int ticketId = ticketBuy.ticket_id;
                TicketModel ticket1 = getTicketById(ticketId);
                ticketBuy.ticket_ = ticket1;
                ticketBuys.add(ticketBuy);
            }
        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }
        for (TicketBuy ticketBuy : ticketBuys) {
            System.out.println("ID: " + ticketBuy.id);
            System.out.println("First Name: " + ticketBuy.firstName);
            System.out.println("Last Name: " + ticketBuy.LastName);
            System.out.println("Father Name: " + ticketBuy.fatherName);
            System.out.println("Passport Number: " + ticketBuy.passportNumber);
            System.out.println("Ticket ID: " + ticketBuy.ticket_id);

            // TicketModel ma'lumotlarini chiqarish
            TicketModel ticket = ticketBuy.ticket_;
            if (ticket != null) {
                System.out.println("Ticket Info:");
                System.out.println("ID: " + ticket.id);
                System.out.println("From: " + ticket.where_from);
                System.out.println("To: " + ticket.where_to);
                System.out.println("When: " + ticket.when);
                System.out.println("Type: " + ticket.ticket_type);
                System.out.println("Flight Number: " + ticket.flight_number);
                System.out.println("Time: " + ticket.time);
                System.out.println("Seat Number: " + ticket.seat_number);
            }

            System.out.println(); // Bo'sh qator chiqarish
        }
        return ticketBuys;
    }

    public ArrayList<TicketBuy> GetTicketAdmin1(TicketBuy model) throws ParseException {
        ArrayList<TicketBuy> ticketBuys = new ArrayList<>();
        try {

            String dateString = model.ticket_.when;
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsed = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            String query = "SELECT * FROM ticketbuy  \n"
                    + "INNER JOIN ticketmodel ON ticketmodel.id=ticketbuy.ticket_id\n"
                    + " WHERE ticketmodel.where_from=? and ticketmodel.where_to=? "
                    + " and ticketmodel.when_date=?\n"
                    + " and ticketmodel.ticket_type=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, model.ticket_.where_from);
            statement.setString(2, model.ticket_.where_to);
            statement.setDate(3, sqlDate);
            statement.setString(4, model.ticket_.ticket_type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TicketBuy ticketBuy = new TicketBuy();
                ticketBuy.id = resultSet.getInt("id");
                ticketBuy.firstName = resultSet.getString("first_name");
                ticketBuy.LastName = resultSet.getString("last_name");
                ticketBuy.fatherName = resultSet.getString("father_name");
                ticketBuy.passportNumber = resultSet.getString("passport_number");
                ticketBuy.ticket_id = resultSet.getInt("ticket_id");

                // TicketModel obyektini o'qib olamiz
                int ticketId = ticketBuy.ticket_id;
                TicketModel ticket1 = getTicketById(ticketId);
                ticketBuy.ticket_ = ticket1;
                ticketBuys.add(ticketBuy);
            }
        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }

        return ticketBuys;
    }

    public Boolean CreateTicket(TicketBuy ticketBuy) throws ParseException {

        String dateString = ticketBuy.ticket_.when;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date parsed = format.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        String query = "INSERT INTO TicketModel (where_from, where_to, when_date, ticket_type, flight_number, time_of, seat_number) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, ticketBuy.ticket_.where_from);
            statement.setString(2, ticketBuy.ticket_.where_to);
            statement.setDate(3, sqlDate);
            statement.setString(4, ticketBuy.ticket_.ticket_type);
            statement.setInt(5, ticketBuy.ticket_.flight_number);

            String timeString = ticketBuy.ticket_.time;
            LocalTime localTime = LocalTime.parse(timeString);
            Time time = Time.valueOf(localTime);

            statement.setTime(6, time);
            statement.setInt(7, ticketBuy.ticket_.seat_number);

            // So'rovni bajarish
            int rowsAffected = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Bilet model muvaffaqiyatli qo'shildi. Id: " + id);
                insertTicketBuy(id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Bilet model qo'shishda xatolik yuz berdi: " + e.getMessage());
            return false;
        }
        return null;
    }

    public Boolean insertTicketBuy(int ticket_id) {
        String query = "INSERT INTO TicketBuy (ticket_id) VALUES (?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ticket_id);
            ResultSet resultSet = statement.executeQuery();
            return true;
        } catch (SQLException e) {
            System.out.println("Ma'lumot qo'shishda xatolik yuz berdi: " + e.getMessage());
        }
        return false;
    }

    public Boolean DeleteTicket(int id) {
        var model = GetById(id);
        System.out.println(model.ticket_id);
        String query = "DELETE FROM ticketbuy ticketmodel WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id); 
            statement.executeUpdate();
            statement.close();

            String query1 = "DELETE FROM ticketmodel WHERE id = ?";
            try {
                PreparedStatement statement1 = connection.prepareStatement(query1);
                statement1.setInt(1, model.ticket_id);
                statement1.executeUpdate(); 
                statement1.close();
                return true;
            } catch (SQLException e) {
                System.out.println("Internal error occurred while deleting data: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("External error occurred while deleting data: " + e.getMessage());
            return false;
        }
    }

    public Boolean UpdateTicket(TicketBuy ticketBuy) {

        String query = "UPDATE ticketbuy SET first_name=?, last_name=?, father_name=?, passport_number=? WHERE id=?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketBuy.firstName);
            statement.setString(2, ticketBuy.LastName);
            statement.setString(3, ticketBuy.fatherName);
            statement.setString(4, ticketBuy.passportNumber);
            statement.setInt(5, ticketBuy.id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("TicketBuy ma'lumoti muvaffaqiyatli yangilandi.");
                return true;
            } else {
                System.out.println("TicketBuy ma'lumoti yangilashda xatolik yuz berdi.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("TicketBuy ma'lumoti yangilashda xatolik yuz berdi: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<TicketBuy> GetMyTicket(String passport_number) {
        ArrayList<TicketBuy> ticketBuys = new ArrayList<>();
        try {

            String query = "SELECT * FROM ticketbuy WHERE passport_number=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, passport_number);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TicketBuy ticketBuy = new TicketBuy();
                ticketBuy.id = resultSet.getInt("id");
                ticketBuy.firstName = resultSet.getString("first_name");
                ticketBuy.LastName = resultSet.getString("last_name");
                ticketBuy.fatherName = resultSet.getString("father_name");
                ticketBuy.passportNumber = resultSet.getString("passport_number");
                ticketBuy.ticket_id = resultSet.getInt("ticket_id");

                // TicketModel obyektini o'qib olamiz
                int ticketId = ticketBuy.ticket_id;
                TicketModel ticket1 = getTicketById(ticketId);
                ticketBuy.ticket_ = ticket1;
                ticketBuys.add(ticketBuy);
            }
        } catch (SQLException e) {
            System.out.println("Ma'lumotlarni o'qishda xatolik yuz berdi: " + e.getMessage());
        }

        return ticketBuys;

    }

    public Boolean UpdateTicketAdmin(TicketBuy ticketModel) throws ParseException {

        TicketBuy model = GetById(ticketModel.id);
        
        String query = "UPDATE ticketmodel SET where_from = ?, where_to = ?, when_date = ?, ticket_type = ?, flight_number = ?, time_of = ?, seat_number = ? WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            String dateString = ticketModel.ticket_.when;
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date parsed = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());

            String timeString = ticketModel.ticket_.time;
            LocalTime localTime = LocalTime.parse(timeString);
            Time time = Time.valueOf(localTime);

            statement.setString(1, ticketModel.ticket_.where_from);
            statement.setString(2, ticketModel.ticket_.where_to);
            statement.setDate(3, sqlDate);
            statement.setString(4, ticketModel.ticket_.ticket_type);
            statement.setInt(5, ticketModel.ticket_.flight_number);
            statement.setTime(6, time);
            statement.setInt(7, ticketModel.ticket_.seat_number);
            statement.setInt(8, model.ticket_id);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Ma'lumotni yangilashda xatolik yuz berdi: " + e.getMessage());
            return false;
        }

    }
}

package me.kyrobi.mio.utils;

import java.io.File;
import java.sql.*;
import java.util.Objects;

public class Sqlite {

    File dbfile = new File(".");
    String url = "jdbc:sqlite:" + dbfile.getAbsolutePath() + "\\counting.db";

    public void createNewTable(){
        String sql = "CREATE TABLE IF NOT EXISTS 'stats' ("
                + " 'userId' TEXT PRIMARY KEY,"
                + " 'amount' integer NOT NULL DEFAULT 0)";

        // the PRIMARY KEY uniquely defines a record

        try{
            Connection conn = DriverManager.getConnection(url); //Tries to open the connection
            Statement stmt = conn.createStatement(); // Formulate the command to execute
            stmt.execute(sql);  //Execute said command
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
        }

        System.out.println("Database does not exist. Creating a new one!");
    }

    public void insert(String id, int amount){

        String sqlcommand = "INSERT INTO stats(userId, amount) VALUES(?,?)";

        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sqlcommand);
            stmt.setString(1, id); // The first column will contain the ID
            stmt.setInt(2, amount); // The second column will contain the amount
            stmt.executeUpdate();
        }
        catch(SQLException error){
            System.out.println(error.getMessage());
        }
    }

//    public void update(String id, int amount){
//
//        String checkIfExist = "SELECT EXISTS(SELECT 1 FROM stats WHERE userId=?)";
//
//        try{
//            Connection conn = DriverManager.getConnection(url);
//            Statement stmt = conn.prepareStatement(checkIfExist);
//
//        }
//        if(){
//
//        }
//
//
//    }

    public int getCount(String Id){
        //String checkIfExist = "SELECT EXISTS(SELECT 1 FROM stats WHERE userId='" + test + "' collate nocase)";

        // String to get all the values from the database
        String selectfrom = "SELECT * FROM stats";

        try{
            System.out.println("Connecting...");
            Connection conn = DriverManager.getConnection(url); // Make connection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectfrom); // Execute the command

            int count = 0;

            //We loop through the database. If the userID matches, we break out of the loop
            while(rs.next()){
                System.out.println("ID: " + rs.getString("userId") + " Amount: " + rs.getInt("amount"));
                if(Objects.equals(rs.getString("userId"), Id)){
                    break; // Breaks out of the loop once the value has been found. No need to loop through the rest of the database
                }
            }

            //System.out.println("Count: " + count);
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error code: " + e.getMessage());
        }
        return 0;
    }
}

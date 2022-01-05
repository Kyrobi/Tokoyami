package me.kyrobi.mio.utils;

import java.io.File;
import java.sql.*;
import java.util.Objects;

public class Sqlite {

    File dbfile = new File(".");
    String url = "jdbc:sqlite:" + dbfile.getAbsolutePath() + "\\counting.db";

    // This function will create a new database if one doesn't exist
    public void createNewTable(){
        String sql = "CREATE TABLE IF NOT EXISTS 'stats' ("
                + " 'userId' integer PRIMARY KEY,"
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

    //Insert a new value into the database
    public void insert(long id, int amount){

        String sqlcommand = "INSERT INTO stats(userId, amount) VALUES(?,?)";

        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sqlcommand);
            stmt.setLong(1, id); // The first column will contain the ID
            stmt.setInt(2, amount); // The second column will contain the amount
            stmt.executeUpdate();
            conn.close();
        }
        catch(SQLException error){
            System.out.println(error.getMessage());
        }
    }

    //Updates an existing value in the database
    public void update(long userId, int amount){

        //Update the data specified by the userId
        String updateCommand = "UPDATE stats SET amount=" +amount + " WHERE userId=" + "'" + userId + "'";

        //System.out.println("Update command: " + updateCommand);

        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(updateCommand);
            stmt.executeUpdate();
            conn.close();

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public int getCount(long Id){
        //String checkIfExist = "SELECT EXISTS(SELECT 1 FROM stats WHERE userId='" + test + "' collate nocase)";

        // String to get all the values from the database
        String selectfrom = "SELECT * FROM stats";
        int count = 0;

        try{
            //System.out.println("Connecting...");
            Connection conn = DriverManager.getConnection(url); // Make connection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectfrom); // Execute the command


            //We loop through the database. If the userID matches, we break out of the loop
            while(rs.next()){
                //System.out.println("ID: " + rs.getString("userId") + " Amount: " + rs.getInt("amount"));
                if(Objects.equals(rs.getLong("userId"), Id)){
                    ++count;
                    rs.close();
                    conn.close();
                    break; // Breaks out of the loop once the value has been found. No need to loop through the rest of the database
                }
            }

            //System.out.println("Count: " + count);
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error code: " + e.getMessage());
        }
        return count;
    }

    public int getAmount(long userId){
        String selectfrom = "SELECT * FROM stats WHERE userId=" + userId;
        int amount = 0;

        try {
            //System.out.println("Connecting...");
            Connection conn = DriverManager.getConnection(url); // Make connection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectfrom); // Execute the command
            amount = rs.getInt("amount");
            rs.close();
            conn.close();
        }
        catch(SQLException se){
            System.out.println(se.getMessage());
        }

        return amount;
    }
}

package sample;

import java.sql.*;
import java.sql.*;

public class Model {
    public enum fieldNameEnum {
        Username,Password,Birthday,FirstName,LastName,City;
    }
    public static void check_connection(String dbPath) {
        Connection dbconnection = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            dbconnection = DriverManager.getConnection(url);
            System.out.println("Database " + dbPath + " Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (dbconnection != null) {
                    dbconnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//checking connection to database with path of the database.

    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//creating a new database with the parameter name

    public static void createNewUsersTable(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/" + fileName;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Users_Table (\n"
                + "Username text PRIMARY KEY,\n"
                + "	Password text NOT NULL,\n"
                + "	Birthday date\n"
                + "	FirstName text NOT NULL,\n"
                + "	LastName text NOT NULL,\n"
                + "	City text NOT NULL,\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//creating a new users table

    private Connection connect(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String DatabaseName, String UserName_input, String Password_input, Date Birthday_input, String FirstName_input, String LastName_input, String City_input) {
        String sql = "INSERT INTO Users_Table(Username,Password,Birthday,FirstName,LastName,City) VALUES(?,?,?,?,?,?)";

        try (Connection conn = this.connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, UserName_input);
            pstmt.setString(2, Password_input);
            pstmt.setDate(3, Birthday_input);
            pstmt.setString(4, FirstName_input);
            pstmt.setString(5, LastName_input);
            pstmt.setString(6, City_input);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getUserInfo(String DatabaseName, fieldNameEnum fieldName, String value) {
        if(fieldName.equals("Birthday"))
            return null;
        return getUserInfo(DatabaseName,fieldName,value,null);
    }

    public String getUserInfo(String DatabaseName, fieldNameEnum fieldName, Date Dvalue) {
        if(!fieldName.equals("Birthday"))
            return null;
        return getUserInfo(DatabaseName,fieldName,"",Dvalue);
    }

    public String getUserInfo(String DatabaseName, fieldNameEnum fieldName, String value, Date Dvalue) {
        String sql = "SELECT " + fieldName
                + "FROM Users_Table WHERE " + fieldName + " = ?";

        try (Connection conn = this.connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            if (fieldName.equals(fieldNameEnum.Birthday)){
                pstmt.setDate(1, Dvalue);
            }
            else {
                pstmt.setString(1, value);
            }
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            String result = "";
            while (rs.next()) {
                if (fieldName.equals(fieldNameEnum.Birthday)){
                    result = rs.getDate("Birthday") +"";
                }
                else{
                    result = rs.getString(fieldName+"");
                }
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "ERROR";
        }
    }

    public void updateUserInfo(String DatabaseName, String UserName_input, String Password_input, Date Birthday_input, String FirstName_input, String LastName_input, String City_input){

    }

    public void deleteUser(String DatabaseName, String UserName_input){

    }

}


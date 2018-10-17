package sample;

import java.sql.*;
import java.sql.*;
import java.util.Observable;

public class Model extends Observable {


    public enum UsersfieldNameEnum {Username,Password,Birthday,FirstName,LastName,City;}
    public enum tableNameEnum{Users_table;}

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

        String url = "jdbc:sqlite:"+ Configuration.loadProperty("directoryPath") + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//creating a new database with the parameter name

    public static void createNewUsersTable(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + Configuration.loadProperty("directoryPath") + fileName;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Users_Table (\n"
                + "Username text PRIMARY KEY,\n"
                + "	Password text NOT NULL,\n"
                + "	Birthday text NOT NULL,\n"
                + "	FirstName text NOT NULL,\n"
                + "	LastName text NOT NULL,\n"
                + "	City text NOT NULL\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//creating a new users table

    private static Connection connect(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:"+ Configuration.loadProperty("directoryPath") + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // TODO: 17-Oct-18 //ten barosh oded;
    public static boolean legalUsername(String databaseName,String tableName,String username) {
        if(selectQuery(databaseName,tableName,"Username","Username='"+username+"'").equals("ERROR")){
            return true;
        }
        return false;
    }

    public static void insert(String DatabaseName, String UserName_input, String Password_input, String Birthday_input, String FirstName_input, String LastName_input, String City_input) {
        String sql = "INSERT INTO Users_Table(Username,Password,Birthday,FirstName,LastName,City) VALUES(?,?,?,?,?,?)";

        try (Connection conn = connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, UserName_input);
            pstmt.setString(2, Password_input);
            pstmt.setString(3, Birthday_input);
            pstmt.setString(4, FirstName_input);
            pstmt.setString(5, LastName_input);
            pstmt.setString(6, City_input);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

////    public static String getUserInfo(String DatabaseName,fieldNameEnum wantedfieldName, fieldNameEnum byfieldName, String value) {
////        if(byfieldName.equals("Birthday"))
////            return null;
////        return getUserInfo(DatabaseName,wantedfieldName,byfieldName,value,null);
////    }
////
////    public static String getUserInfo(String DatabaseName, fieldNameEnum fieldName, Date Dvalue) {
////        if(!fieldName.equals("Birthday"))
////            return null;
////        return getUserInfo(DatabaseName,fieldName,"",Dvalue);
//    }

    public static String selectQuery(String DatabaseName, String tableName ,String wantedfields, String whereCondition) {
        String sql = "SELECT " + wantedfields
                + " FROM " + tableName +" WHERE " + whereCondition ;

        try (Connection conn = connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            String result = "";
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber;
            columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) result += ",  ";
                    String columnValue = rs.getString(i);
                    result += columnValue;
                }
                result += '\n';
            }
            return result;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "ERROR";
        }
    }

    public void updateUserInfo(String DatabaseName, String UserName_key, String UserName_input, String Password_input, Date Birthday_input, String FirstName_input, String LastName_input, String City_input){
        String sql = "UPDATE Users_Table SET UserName = ? , "
                + "Password = ? "
                + "Birthday = ? "
                + "FirstName = ? "
                + "LastName = ? "
                + "City = ? "
                + "WHERE UserName = ?";

        try (Connection conn = this.connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, UserName_input);
            pstmt.setString(2, Password_input);
            pstmt.setDate(3, Birthday_input);
            pstmt.setString(4, FirstName_input);
            pstmt.setString(5, LastName_input);
            pstmt.setString(6, City_input);
            pstmt.setString(7, UserName_key);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(String DatabaseName,String tableName, String UserName_key, String whereCondition){
        String sql = "DELETE FROM " + tableName+" WHERE "+ whereCondition;

        try (Connection conn = connect(DatabaseName);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, UserName_key);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


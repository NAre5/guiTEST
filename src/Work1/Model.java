package Work1;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model implements IModel {
    //attributes
    private String databaseName;
    //Enums...
    public enum UsersfieldNameEnum {Username,Password,Birthday,FirstName,LastName,City;}

    public enum tableNameEnum{Users_table;}
    //constructor
    public Model(String databaseName) {
        this.databaseName = databaseName+".db";
        createNewDatabase();
    }
    // private functions (generics)
    private void createNewDatabase() {

        String url = "jdbc:sqlite:"+ Configuration.loadProperty("directoryPath") + databaseName;

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//creating a new database with the parameter name

    public void createNewUsersTable() {
        // SQLite connection string
        Connection c = null;
        Statement stmt = null;
        String url = "jdbc:sqlite:" + Configuration.loadProperty("directoryPath") + databaseName;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:"+ Configuration.loadProperty("directoryPath") + databaseName);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users_Table (\n"
                    + "Username text PRIMARY KEY,\n"
                    + "	Password text NOT NULL,\n"
                    + "	Birthday text NOT NULL,\n"
                    + "	FirstName text NOT NULL,\n"
                    + "	LastName text NOT NULL,\n"
                    + "	City text NOT NULL\n"
                    + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }//creating a new users table

    private void insertQuery(String table_name,Class<? extends Enum<?>> tableEnum, String[] insert_values) {
        String [] field_array = getNames(tableEnum);
        String sql = "INSERT INTO "+table_name+"(";
        for (int i=0;i<field_array.length;i++){
            sql += field_array[i] + ",";
        }
        sql = sql.substring(0,sql.length()-1);
        sql += ") VALUES(";
        for (int i=0;i<field_array.length;i++){
            if (i != field_array.length -1)
                sql +=  "?,";
            else
                sql += "?)";
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i=0;i<field_array.length;i++){
                pstmt.setString(i+1, insert_values[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//insert query pattern

    private List<String[]> selectQuery(String table_name, String where_condition) {
        String sql = "SELECT *" + " FROM " + table_name +" WHERE " + where_condition ;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            int nCol = rs.getMetaData().getColumnCount();
            List<String[]> table = new ArrayList<>();
            while( rs.next()) {
                String[] row = new String[nCol];
                for( int iCol = 1; iCol <= nCol; iCol++ ){
                    Object obj = rs.getObject( iCol );
                    row[iCol-1] = (obj == null) ?null:obj.toString();
                }
                table.add( row );
            }
            return table;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }//select query pattern

    private void updateQuery(String table_name,Class<? extends Enum<?>> tableEnum, String[] update_values, String where_condition) {
        String [] fields_array = getNames(tableEnum);
        String sql = "UPDATE "+table_name+" SET ";
        for (int i=0;i<fields_array.length;i++){
            sql += fields_array[i] + " = ? ,";
        }
        sql = sql.substring(0,sql.length()-1) + " ";
        sql += "WHERE " + where_condition;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            for (int j=0;j<fields_array.length;j++){
                pstmt.setString(j+1, update_values[j]);
            }
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//update query pattern

    private static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }//converting enum to String array

    private void deleteQuery(String table_name, String where_condition) {
        String sql = "DELETE FROM " + table_name+" WHERE "+where_condition ;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//delete query pattern

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+ Configuration.loadProperty("directoryPath") + databaseName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }//connecting to the database
    //public Functions:

    // Users_Table
    @Override
    public void UsersTable_createUser(String Username_val, String Password_val, String Birthday_val, String FirstName_val, String LastName_val, String City_val) {
        String [] values = {Username_val,Password_val,Birthday_val,FirstName_val,LastName_val,City_val};
        insertQuery("Users_Table",UsersfieldNameEnum.class,values);
    }

    @Override
    public String[] UsersTable_getUserByUsername(String Username_val) {
        List<String[]> result = selectQuery("Users_Table",UsersfieldNameEnum.Username + " = '" + Username_val+"'");
        if(result.size() != 1)
            return null;
        else
            return result.get(0);
    }

    @Override
    public void UsersTable_updateUserInfoByUsername(String Username_key, String Username_val, String Password_val, String Birthday_val, String FirstName_val, String LastName_val, String City_val) {
        String [] values = {Username_val,Password_val,Birthday_val,FirstName_val,LastName_val,City_val};
        updateQuery(tableNameEnum.Users_table.toString(),UsersfieldNameEnum.class,values,UsersfieldNameEnum.Username.toString() + " = '" + Username_key+"'");
    }

    @Override
    public void UsersTable_deleteUserByUsername(String Username_key) {
        deleteQuery(tableNameEnum.Users_table.toString(),UsersfieldNameEnum.Username + " = '" + Username_key + "'");
    }

    @Override
    public boolean UsersTable_existingUsername(String username) {
        if(UsersTable_getUserByUsername(username) == null)
            return false;
        else
            return true;
    }

    public boolean UsersTable_checkPassword(String Username_val,String Password_val) {
        List<String[]> result = selectQuery("Users_Table",UsersfieldNameEnum.Username + " = '" + Username_val+"'");
        if(result.size() != 1)
            return false;
        else if(result.get(0)[1].equals(Password_val)==false)
            return false;
        return true;
    }


}

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class Controller implements Initializable {
    public TextField username;
    public TextField password;
    public TextField firstname;
    public TextField lastname;
    public TextField birthdate;
    public TextField city;
    List<TextField> fields = new ArrayList<>();
    final String directoryPath = "C:/DATABASE/";
    final String databaseName = "database.db";
    final String tableName = "users";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fields.add(username);
        fields.add(password);
        fields.add(firstname);
        fields.add(lastname);
        fields.add(birthdate);
        fields.add(city);
        //create directory
        try {
            Files.createDirectory(Paths.get(directoryPath));//check if ok to end with '/'
        } catch (IOException ignored) {
        }
        //create database
        if (!Files.exists(Paths.get(directoryPath+databaseName)))
            ;//create db file;

        //create table

    }


    public void submitValues(ActionEvent actionEvent) {
        //trim all fields from spaces ((not must))
        for (TextField field : fields) {
            String newText = field.getText().trim();
            field.setText(newText);

        }

        Connection conn;

        final String url = "jdbc:sqlite:" + directoryPath + databaseName;
        try {
            conn = DriverManager.getConnection(url);
            StringJoiner values = new StringJoiner(",", "", "");
            for (TextField field : fields) {
                values.add(field.getText());
            }


            String insertQuery = "INSERT INTO " + tableName +
                    " (" + values.toString() + ") " +
                    "VALUES (?,?,?,?,?,?);";//check///////////
            PreparedStatement ps = conn.prepareStatement(insertQuery);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

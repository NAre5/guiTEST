package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Controller implements Initializable {
    public TabPane tabPane;
    public Tab signTab;
    public Tab homeTab;
    public Tab createTab;
    public Tab readTab;
    public Tab updateTab;
    public TextField usernameSign;
    public TextField passwordSign;
    public Button submit;
    public Label usernameHome;
    public Label firstHome;
    public Label lastHome;
    public Label birthHome;
    public Label cityHome;
    public Button delete;
    public Button signOut;
    public TextField usernameCreate;
    public TextField passwordCreate;
    public TextField confirmCreate;
    public TextField firstCreate;
    public TextField lastCreate;
    public TextField birthCreate;
    public TextField cityCreate;
    public Button create;
    public TextField usernameRead;
    public Button show;
    public Label firstRead;
    public Label lastRead;
    public Label birthRead;
    public Label cityRead;
    public TextField usernameUpdate;
    public TextField passwordUpdate;
    public TextField firstUpdate;
    public TextField lastUpdate;
    public TextField birthUpdate;
    public TextField cityUpdate;
    public Button update;


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
    Model model;

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
        Model.createNewDatabase(databaseName);
        Model.createNewUsersTable(tableName);
        tabSignOut();
    }

    public void tabSignOut(){
        tabPane.getTabs().remove(0,5);
        tabPane.getTabs().add(signTab);
        tabPane.getTabs().add(createTab);
        createTab.setText("Sign up");
        create.setText("Sign up!");
        create.setOnAction(this::signUp);
    }

    public void tabSignIn(){
        tabPane.getTabs().remove(0,2);
        tabPane.getTabs().add(homeTab);
        tabPane.getTabs().add(createTab);
        tabPane.getTabs().add(readTab);
        tabPane.getTabs().add(updateTab);
        createTab.setText("Create");
        create.setText("Create!");
        create.setOnAction(this::create);
    }

    public void signUp(ActionEvent event){
        create(event);
        signIn(event,usernameCreate.getText(),passwordCreate.getText());
    }

    public void create(ActionEvent event) {
        if(passwordCreate.getText().equals(confirmCreate.getText())==false){
            confirm("Password must be match in both options");
            event.consume();
        }
        else if(Model.legalUsername(databaseName,tableName,usernameCreate.getText())==false){
            confirm("Username already taken");
            event.consume();
        }
        else{
            Model.insert(databaseName,usernameCreate.getText(),passwordCreate.getText(),birthCreate.getText(),firstCreate.getText(),lastCreate.getText(),cityCreate.getText());
        }

    }

    public void submit(ActionEvent event){
        signIn(event,usernameSign.getText(),passwordSign.getText());
    }

    private void signIn(ActionEvent event, String username, String password) {
        if(Model.legalUsername(databaseName,tableName,usernameSign.getText())==false){
            confirm("Username is incorrect");
            event.consume();
        }
        else if(Model.selectQuery(databaseName,tableName,"password",username).equals(password)==false){
            confirm("Username or Password are incorrect");
            event.consume();
        }
        else{
            tabSignIn();
            updateHome(username);
        }
    }

    public void signOut(ActionEvent event){

    }

    public void delete(ActionEvent event){

    }

    public void show(ActionEvent event){

    }
    public void update(ActionEvent event){

    }

    private void updateHome(String username) {
        firstHome.setText(Model.selectQuery(databaseName,tableName,"FirstName",username));
        lastHome.setText(Model.selectQuery(databaseName,tableName,"LastName",username));
        birthHome.setText(Model.selectQuery(databaseName,tableName,"Birthday",username));
        cityHome.setText(Model.selectQuery(databaseName,tableName,"City",username));
    }

    public boolean confirm(String str){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(str);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
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

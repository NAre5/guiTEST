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
    final String directoryPath = "C:/DATABASE/";//////
    final String databaseName = "database.db";
    final String tableName = "Users_Table";
    Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model=new Model();
        fields.add(username);
        fields.add(password);
        fields.add(firstname);
        fields.add(lastname);
        fields.add(birthdate);
        fields.add(city);
        //create directory
//        try {
//            Files.createDirectory(Paths.get(directoryPath));//check if ok to end with '/'
//        } catch (IOException ignored) {
//        }
//        //create database
//        if (!Files.exists(Paths.get(directoryPath+databaseName)))
//            ;//create db file;

        //create table
        model.createNewDatabase(databaseName);
        Model.createNewUsersTable(databaseName);
        tabSignOut();/////
    }

    public void tabSignOut(){
        clearAll();
        tabPane.getTabs().remove(0,tabPane.getTabs().size());
        tabPane.getTabs().add(signTab);
        tabPane.getTabs().add(createTab);
        createTab.setText("Sign up");
        create.setText("Sign up!");
        create.setOnAction(this::signUp);
        homeTab.setClosable(false);
        createTab.setClosable(false);
        signTab.setClosable(false);
        readTab.setClosable(false);
        updateTab.setClosable(false);
    }

    public void tabSignIn(){
        clearAll();
        tabPane.getTabs().remove(0,2);
        tabPane.getTabs().add(homeTab);
//        tabPane.getTabs().add(createTab);
        tabPane.getTabs().add(readTab);
        tabPane.getTabs().add(updateTab);
//        createTab.setText("Create");
//        create.setText("Create!");
//        create.setOnAction(this::create);
    }

    public void signUp(ActionEvent event){
        if(create(event)){
            signIn(event,usernameCreate.getText(),passwordCreate.getText());
        }
    }

    private void clearAll() {
        clearSignIn();
        clearHome();
        clearCreate();
        clearRead();
        clearUpdate();
    }

    private void clearSignIn() {
        usernameSign.clear();
        passwordSign.clear();
    }

    private void clearHome() {
        usernameHome.setText("");
        firstHome.setText("");
        lastHome.setText("");
        birthHome.setText("");
        cityHome.setText("");
    }

    private void clearCreate() {
        usernameCreate.clear();
        passwordCreate.clear();
        confirmCreate.clear();
        lastCreate.clear();
        firstCreate.clear();
        cityCreate.clear();
        birthCreate.clear();
    }

    private void clearRead() {
        usernameRead.clear();
        firstRead.setText("");
        lastRead.setText("");
        birthRead.setText("");
        cityRead.setText("");
    }

    private void clearUpdate() {
        usernameUpdate.clear();
        passwordUpdate.clear();
        firstUpdate.clear();
        lastUpdate.clear();
        birthUpdate.clear();
        cityUpdate.clear();
    }

    public boolean create(ActionEvent event) {
        if(passwordCreate.getText().equals(confirmCreate.getText())==false){
            confirm("Password must be match in both options");
            event.consume();
        }
        else if(Model.existingUsername(databaseName,tableName,usernameCreate.getText())==true){
            confirm("Username already taken");
            event.consume();
        }
        else{
            Model.insert(databaseName,usernameCreate.getText(),passwordCreate.getText(),birthCreate.getText(),firstCreate.getText(),lastCreate.getText(),cityCreate.getText());
            return true;
        }
        return false;
    }

    public void submit(ActionEvent event){
        signIn(event,usernameSign.getText(),passwordSign.getText());
    }

    private void signIn(ActionEvent event, String username, String password) {
        if(Model.existingUsername(databaseName,tableName,username)==false){
            confirm("Username is incorrect");
            event.consume();
        }
        else if(Model.selectQuery(databaseName,tableName,"Password","UserName='"+username+"'").equals(password)==false){
            confirm("Username or Password are incorrect");
            event.consume();
        }
        else{
            tabSignIn();
            updateHome(username);
        }
    }

    public void signOut(ActionEvent event){
        tabSignOut();
        event.consume();
    }

    public void delete(ActionEvent event){

    }

    public void show(ActionEvent event){
        if(Model.existingUsername(databaseName,tableName,usernameRead.getText())==true){
            firstRead.setText(Model.selectQuery(databaseName,tableName,"FirstName","UserName='"+usernameRead.getText()+"'"));
            lastRead.setText(Model.selectQuery(databaseName,tableName,"LastName","UserName='"+usernameRead.getText()+"'"));
            birthRead.setText(Model.selectQuery(databaseName,tableName,"Birthday","UserName='"+usernameRead.getText()+"'"));
            cityRead.setText(Model.selectQuery(databaseName,tableName,"City","UserName='"+usernameRead.getText()+"'"));
        }
        else {
            confirm("Username is incorrect");
            event.consume();
        }
    }
    public void update(ActionEvent event){

    }

    private void updateHome(String username) {
        usernameHome.setText(username);
        firstHome.setText(Model.selectQuery(databaseName,tableName,"FirstName","UserName='"+username+"'"));
        lastHome.setText(Model.selectQuery(databaseName,tableName,"LastName","UserName='"+username+"'"));
        birthHome.setText(Model.selectQuery(databaseName,tableName,"Birthday","UserName='"+username+"'"));
        cityHome.setText(Model.selectQuery(databaseName,tableName,"City","UserName='"+username+"'"));
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

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

    //tabs
    public TabPane tabPane;
    public Tab signTab;
    public Tab homeTab;
    public Tab createTab;
    public Tab readTab;
    public Tab updateTab;
    //Sign in tab
    public TextField usernameSign;
    public TextField passwordSign;
    public Button submit;
    //Home tab
    public Label usernameHome;
    public Label firstHome;
    public Label lastHome;
    public Label birthHome;
    public Label cityHome;
    public Button delete;
    public Button signOut;
    //Create tab
    public TextField usernameCreate;
    public TextField passwordCreate;
    public TextField confirmCreate;
    public TextField firstCreate;
    public TextField lastCreate;
    public TextField birthCreate;
    public TextField cityCreate;
    public Button create;
    //Read tab
    public TextField usernameRead;
    public Button show;
    public Label firstRead;
    public Label lastRead;
    public Label birthRead;
    public Label cityRead;
    //update tab
    public TextField usernameUpdate;
    public TextField passwordUpdate;
    public TextField firstUpdate;
    public TextField lastUpdate;
    public TextField birthUpdate;
    public TextField cityUpdate;
    public Button update;

    final String directoryPath = "C:/DATABASE/";//////
    final String databaseName = "database.db";
    final String tableName = "Users_Table";
    Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabSignOut();
    }

    public void setModel(Model model){
        this.model=model;
        model.createNewDatabase();
        model.createNewUsersTable();
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
        else if(model.existingUsername(tableName,usernameCreate.getText())==true){
            confirm("Username already taken");
            event.consume();
        }
        else{
            model.insert(usernameCreate.getText(),passwordCreate.getText(),birthCreate.getText(),firstCreate.getText(),lastCreate.getText(),cityCreate.getText());
            return true;
        }
        return false;
    }

    public void submit(ActionEvent event){
        signIn(event,usernameSign.getText(),passwordSign.getText());
    }

    private void signIn(ActionEvent event, String username, String password) {
        if(model.existingUsername(tableName,username)==false){
            confirm("Username is incorrect");
            event.consume();
        }
        else if(model.selectQuery(tableName,"Password","UserName='"+username+"'").equals(password)==false){
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
        if(model.existingUsername(tableName,usernameRead.getText())==true){
            firstRead.setText(model.selectQuery(tableName,"FirstName","UserName='"+usernameRead.getText()+"'"));
            lastRead.setText(model.selectQuery(tableName,"LastName","UserName='"+usernameRead.getText()+"'"));
            birthRead.setText(model.selectQuery(tableName,"Birthday","UserName='"+usernameRead.getText()+"'"));
            cityRead.setText(model.selectQuery(tableName,"City","UserName='"+usernameRead.getText()+"'"));
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
        firstHome.setText(model.selectQuery(tableName,"FirstName","UserName='"+username+"'"));
        lastHome.setText(model.selectQuery(tableName,"LastName","UserName='"+username+"'"));
        birthHome.setText(model.selectQuery(tableName,"Birthday","UserName='"+username+"'"));
        cityHome.setText(model.selectQuery(tableName,"City","UserName='"+username+"'"));
    }

    public boolean confirm(String str){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(str);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

//    public void submitValues(ActionEvent actionEvent) {
//        //trim all fields from spaces ((not must))
//        for (TextField field : fields) {
//            String newText = field.getText().trim();
//            field.setText(newText);
//
//        }
//
//        Connection conn;
//
//        final String url = "jdbc:sqlite:" + directoryPath + databaseName;
//        try {
//            conn = DriverManager.getConnection(url);
//            StringJoiner values = new StringJoiner(",", "", "");
//            for (TextField field : fields) {
//                values.add(field.getText());
//            }
//
//
//            String insertQuery = "INSERT INTO " + tableName +
//                    " (" + values.toString() + ") " +
//                    "VALUES (?,?,?,?,?,?);";//check///////////
//            PreparedStatement ps = conn.prepareStatement(insertQuery);
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}

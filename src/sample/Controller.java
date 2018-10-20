package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import sample.Model;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

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
    public PasswordField passwordSign;
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
    public DatePicker birthCreate;
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
    public TextField confirmUpdate;
    public TextField firstUpdate;
    public TextField lastUpdate;
    public DatePicker birthUpdate;
    public TextField cityUpdate;
    public Button update;

    final String directoryPath = "C:/DATABASE/";//////
    final String databaseName = "database.db";
    final String tableName = "Users_Table";
    Model model;
    String username="";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabSignOut();
        String pattern = "dd-MM-yyyy";
        changeDateFormat(birthCreate,pattern);
        changeDateFormat(birthUpdate,pattern);

        username="";
    }

    private void changeDateFormat(DatePicker dp, String pattern)
    {
        dp.setPromptText(pattern.toLowerCase());

        dp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        dp.setPromptText(pattern);
    }

    public void setModel(Model model){
        this.model=model;
        model.createNewUsersTable();
    }

    public void tabSignOut(){
        clearAll();
        username="";
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
            event.consume();
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
        birthCreate.setValue(null);
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
        birthUpdate.setValue(null);
        cityUpdate.clear();
    }

    public boolean create(ActionEvent event) {
        if(createEmpty()==true){
            error("Please fill all the fields");
            event.consume();
        }
        else if(passwordCreate.getText().equals(confirmCreate.getText())==false){
            error("Password must be match in both options");
            event.consume();
        }
        else if(model.UsersTable_existingUsername(usernameCreate.getText())==true){
            error("Username already taken");
            event.consume();
        }
        else if(confirm("Are you sure you want to Sign up with those details?")){
            model.UsersTable_createUser(usernameCreate.getText(),passwordCreate.getText(),DatePicker2Str(birthCreate),firstCreate.getText(),lastCreate.getText(),cityCreate.getText());
            return true;
        }
        return false;
    }

    private String DatePicker2Str(DatePicker value) {
        return (value.getValue().getMonthValue()+"/"+value.getValue().getDayOfMonth()+"/"+value.getValue().getYear());
    }

    private boolean createEmpty() {
        if(usernameCreate.getText().isEmpty() || passwordCreate.getText().isEmpty() || firstCreate.getText().isEmpty() || lastCreate.getText().isEmpty() || birthCreate.getValue().toString().isEmpty() || cityCreate.getText().isEmpty())
            return true;
        else
            return false;
    }
    private boolean updateEmpty() {
        if(usernameUpdate.getText().isEmpty() || passwordUpdate.getText().isEmpty() || firstUpdate.getText().isEmpty() || lastUpdate.getText().isEmpty() || birthUpdate.getValue().toString().isEmpty() || cityUpdate.getText().isEmpty())
            return true;
        else
            return false;
    }

    public void submit(ActionEvent event){
        signIn(event,usernameSign.getText(),passwordSign.getText());
    }

    private void signIn(ActionEvent event, String username, String password) {
        if(model.UsersTable_existingUsername(username)==false){
            error("Username is incorrect");
            event.consume();
        }
        else if(model.UsersTable_checkPassword(username,passwordSign.getText())==false && model.UsersTable_checkPassword(username,passwordCreate.getText())==false){
            error("Username or Password are incorrect");
            event.consume();
        }
        else{
            this.username=username;
            tabSignIn();
            updateHome(username);
            fillUpdate(username);
        }
    }

    private void fillUpdate(String username) {
        usernameUpdate.setText(username);
        passwordUpdate.setText(getPassword(username));
        confirmUpdate.setText(getPassword(username));
        firstUpdate.setText(getFirstName(username));
        lastUpdate.setText(getLastName(username));
        birthUpdate.setValue(parseBirthday(username));
        cityUpdate.setText(getCity(username));
    }

    private LocalDate parseBirthday(String username) {
        String[] details=getBirthday(username).split("/");
        return LocalDate.of(Integer.parseInt(details[2]),Integer.parseInt(details[0]),Integer.parseInt(details[1]));
    }


    public void signOut(ActionEvent event){
        if(confirm("Are you sure you want to sign-out?"))
            tabSignOut();
        event.consume();
    }

    public void delete(ActionEvent event){
        if(confirm("Are you sure you want to delete your account?")){
            info("Your account was deleted successfully!");
            model.UsersTable_deleteUserByUsername(username);
            tabSignOut();
        }
        event.consume();
    }

    public void show(ActionEvent event){
        if(model.UsersTable_existingUsername(usernameRead.getText())==true){
            firstRead.setText(getFirstName(username));
            lastRead.setText(getLastName(username));
            birthRead.setText(getBirthday(username));
            cityRead.setText(getCity(username));
        }
        else {
            error("Username is incorrect");
            event.consume();
        }
    }
    public void update(ActionEvent event){
        if(updateEmpty()==true){
            info("Please fill all the fields");
            event.consume();
        }
        else if(model.UsersTable_existingUsername(usernameUpdate.getText())==true && usernameUpdate.getText().equals(this.username)==false){
            error("Username already taken");
            event.consume();
        }
        else if(passwordUpdate.getText().equals(confirmUpdate.getText())==false){
            error("Password must be match in both options");
            event.consume();
        }
        else if(confirm("Are you sure you want to update the details?")){
            model.UsersTable_updateUserInfoByUsername(username,usernameUpdate.getText(),passwordUpdate.getText(),DatePicker2Str(birthUpdate),firstUpdate.getText(),lastUpdate.getText(),cityUpdate.getText());
            username=usernameUpdate.getText();
            updateHome(username);
            info("The update was made successfully!");
        }
        event.consume();
    }

    private void updateHome(String username) {
        usernameHome.setText(username);
        firstHome.setText(getFirstName(username));
        lastHome.setText(getLastName(username));
        birthHome.setText(getBirthday(username));
        cityHome.setText(getCity(username));
    }

    public boolean confirm(String massage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(massage);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public void error(String massage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(massage);
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void info(String massage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(massage);
        Optional<ButtonType> result = alert.showAndWait();
    }

    private String[] getDetails(String username){
        return model.UsersTable_getUserByUsername(username);
    }

    private String getPassword(String username){
        return model.UsersTable_getUserByUsername(username)[1];
    }

    private String getBirthday(String username){
        return model.UsersTable_getUserByUsername(username)[2];
    }

    private String getFirstName(String username){
        return model.UsersTable_getUserByUsername(username)[3];
    }

    private String getLastName(String username){
        return model.UsersTable_getUserByUsername(username)[4];
    }

    private String getCity(String username){
        return model.UsersTable_getUserByUsername(username)[5];
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

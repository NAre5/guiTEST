package Test;
import sample.Model;

import java.sql.Date;

public class testModel {
    public static void main(String[] args) {
        Model model=new Model("testDatabase");
        model.createNewDatabase();
        model.createNewUsersTable();
        //model.insert("ronenTHEman","1264","1/1/1958","ronen","sarusi","Meitar");
        //model.insert("oded125","9567","24/9/1998","oded","blumenthal","Eilat");
        String res = model.selectQuery(Model.tableNameEnum.Users_table.toString(),Model.UsersfieldNameEnum.Username + "," + Model.UsersfieldNameEnum.Password,"1=1");
        System.out.println(res);
    }
}

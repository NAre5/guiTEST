package Test;
import sample.Model;

import java.sql.Date;
import java.util.Arrays;

public class testModel {
    public enum x1 {a1,a2,a3;}
    public enum x2 {b1,b2,b3;}
    public static void main(String[] args) {
//        Model model=new Model("testDatabase");
//        model.createNewDatabase();
//        model.createNewUsersTable();
//        //model.insert("ronenTHEman","1264","1/1/1958","ronen","sarusi","Meitar");
//        //model.insert("oded125","9567","24/9/1998","oded","blumenthal","Eilat");
//        String res = model.selectQuery(Model.tableNameEnum.Users_table.toString(),Model.UsersfieldNameEnum.Username + "," + Model.UsersfieldNameEnum.Password,"1=1");
//        System.out.println(res);
        INewModel nm = new NewModel("TestVecation4U");
        //nm.UsersTable_createUser("sherlord","654321","12/3/1998","eran","Taganski","Rishon Le-Zion");
        System.out.println(nm.UsersTable_existingUsername("blackjoker94"));
        System.out.println(nm.UsersTable_existingUsername("sherlord1"));
        System.out.println(nm.UsersTable_existingUsername("sherlord"));
      //  System.out.println(res[NewModel.UsersfieldNameEnum.LastName.ordinal()]);

    }
//    public static String[] getNames(Class<? extends Enum<?>> e) {
//        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
//    }
}

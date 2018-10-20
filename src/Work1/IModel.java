package Work1;


public interface IModel {
    //Users Actions
    void UsersTable_createUser(String Username_val,String Password_val,String Birthday_val, String FirstName_val,String LastName_val,String City_val);
    String[] UsersTable_getUserByUsername(String Username_val);
    void UsersTable_updateUserInfoByUsername(String Username_key, String Username_val,String Password_val,String Birthday_val, String FirstName_val,String LastName_val,String City_val);
    void UsersTable_deleteUserByUsername(String Username_val);
    boolean UsersTable_existingUsername(String username);

}

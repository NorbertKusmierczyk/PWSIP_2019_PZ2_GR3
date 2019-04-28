package desktopApp.implementation;

import desktopApp.api.IUser;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User implements IUser {

    public IntegerProperty id;
    public StringProperty userName, email;


    public StringProperty searchUsers = new SimpleStringProperty(this, "bolek", "");


    public StringProperty searchUsersProperty() {
        return searchUsers;
    }

    public void setSearchUsers(StringProperty searchUsers) {
        this.searchUsers = searchUsers;
    }

    public User(){ }

    public User(int id, String userName, String email){
        this.id = new SimpleIntegerProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.email = new SimpleStringProperty(email);
    }

    @Override
    public int getId() {
        return id.get();
    }

    @Override
    public String getUserName() {
        return userName.get();
    }

    @Override
    public String getEmail() {
        return email.get();
    }
}

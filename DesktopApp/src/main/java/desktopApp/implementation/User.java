package desktopApp.implementation;

import desktopApp.api.IUser;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class User implements IUser {

    private IntegerProperty id;
    private StringProperty userName, email, password;
    private Date date;


    public StringProperty searchUsers = new SimpleStringProperty(this, "user", "");


    public StringProperty searchUsersProperty() {
        return searchUsers;
    }

    public void setSearchUsers(StringProperty searchUsers) {
        this.searchUsers = searchUsers;
    }

    public User(){ }

    public User(int id, String userName, String email, Date date, String password){
        this.id = new SimpleIntegerProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.email = new SimpleStringProperty(email);
        this.date = date;
        this.password = new SimpleStringProperty(password);
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

    @Override
    public Date getDate() { return date; }

    @Override
    public String getPassword() { return password.get(); }
}

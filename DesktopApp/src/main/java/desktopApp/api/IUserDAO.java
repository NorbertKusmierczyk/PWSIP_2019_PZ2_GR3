package desktopApp.api;

import desktopApp.implementation.Orders;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;
import javafx.collections.ObservableList;

import java.util.List;

public interface IUserDAO {

    public List<User> setAllUsers();

    public List<Orders> getAllOrders();

    public List<UserOrderDate> getAllOrdersDate();


}

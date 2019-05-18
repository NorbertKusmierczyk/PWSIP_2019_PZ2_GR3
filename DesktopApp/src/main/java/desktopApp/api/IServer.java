package desktopApp.api;

import desktopApp.implementation.Orders;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;

import java.util.List;

public interface IServer {

    public List<User> getAllUsers();

    public List<Orders> getAllOrders();

    public List<UserOrderDate> getAllOrdersDate();
}

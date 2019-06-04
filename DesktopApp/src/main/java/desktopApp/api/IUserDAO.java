package desktopApp.api;

import desktopApp.implementation.Orders;
import desktopApp.implementation.Products;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;
import javafx.collections.ObservableList;

import java.util.List;

public interface IUserDAO {

    public List<User> setAllUsers();

    public List<Orders> getAllOrders();

    public List<UserOrderDate> getAllOrdersDate();

    public List<Products> getAllProducts();

    public void editProduct(int id, String name, int price, int oldId);

    public String findUser(String name);


}

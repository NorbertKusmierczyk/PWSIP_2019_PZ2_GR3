package desktopApp.api;

import desktopApp.implementation.Orders;
import desktopApp.implementation.Products;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;

import java.util.List;

public interface IServer {

    public List<User> getAllUsers();

    public List<Orders> getAllOrders();

    public List<UserOrderDate> getAllOrdersDate();

    public List<Products> getAllProducts();

    public void editProduct(int id, String name, int price, int oldId);

    public String findUser(String name);
}

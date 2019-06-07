package desktopApp.api;

import desktopApp.implementation.Orders;
import desktopApp.implementation.Products;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;

import java.util.List;

public interface IServer {

      List<User> getAllUsers();

      List<Orders> getAllOrders();

      List<UserOrderDate> getAllOrdersDate();

      List<Products> getAllProducts();

      void editProduct(int id, String name, int price, int oldId);

      String findUser(String name);

      int countAllUsers();

      int countAllProducts();

      int countCurrentOrders();

      int setDashBoardGauge();
}

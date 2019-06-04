package desktopApp.implementation;

import desktopApp.api.IServer;
import desktopApp.api.IUserDAO;
import desktopApp.controlers.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Server implements IServer {

    @Autowired
    private IUserDAO userDAO;


    @Override
    public List<User> getAllUsers() { return userDAO.setAllUsers(); }

    @Override
    public List<Orders> getAllOrders() { return userDAO.getAllOrders(); }

    @Override
    public List<UserOrderDate> getAllOrdersDate() { return userDAO.getAllOrdersDate(); }

    @Override
    public List<Products> getAllProducts() { return userDAO.getAllProducts(); }

    @Override
    public void editProduct(int id, String name, int price, int oldId) { userDAO.editProduct(id, name, price, oldId); }

    @Override
    public String findUser(String name) { return userDAO.findUser(name); }
}

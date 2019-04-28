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
    public List<User> getAllUsers() {
        return userDAO.setAllUsers();
    }
}

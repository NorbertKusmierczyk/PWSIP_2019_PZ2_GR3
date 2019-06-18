package desktopApp;

import desktopApp.config.Config;
import desktopApp.controlers.LoginBarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoaderFXML {

    @Autowired
    private AnnotationConfigApplicationContext context;

    public FXMLLoader getLoader(String path){
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource(path));
        return loader;
    }


}

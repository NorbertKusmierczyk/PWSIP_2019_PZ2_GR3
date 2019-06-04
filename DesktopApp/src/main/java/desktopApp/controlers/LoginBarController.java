package desktopApp.controlers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCryptFormatter;
import at.favre.lib.crypto.bcrypt.BCryptParser;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import desktopApp.LoaderFXML;
import desktopApp.api.IServer;
import desktopApp.implementation.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginBarController {

    @FXML
    JFXPasswordField passwordField;

    @FXML
    JFXTextField loginField;

    @FXML
    AnchorPane loginMainPane;

    @Autowired
    AnnotationConfigApplicationContext context;

    @Autowired
    IServer server;

    public void openBoardScene(ActionEvent actionEvent) throws Exception{


       try{

//            String pass = server.findUser(loginField.getText());
//
//            if (BCrypt.verifyer().verify(passwordField.getText().getBytes(), pass.getBytes()).verified){
                Platform.runLater(new Runnable() {
                    public void run() {
                        Parent root = null;


                        try {
                            FXMLLoader loader = context.getBean(LoaderFXML.class).getLoader("/splash.fxml");
                            //root = FXMLLoader.load(getClass().getResource("/splash.fxml"));
                            root = loader.load();
                            final Scene scene = new Scene(root);
                            final Stage stage = new Stage();
                            stage.initStyle(StageStyle.UNDECORATED);

                            stage.setResizable(true);

                            stage.setScene(scene);
                            stage.show();
                            loginMainPane.getScene().getWindow().hide();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
//            }else {
//                Alert globalAlert = new Alert(Alert.AlertType.ERROR);
//                globalAlert.dialogPaneProperty().get();
//                globalAlert.setTitle("Błąd");
//                globalAlert.setHeaderText("Podano błędne hasło");
//                globalAlert.setResizable(false);
//                globalAlert.show();
//                globalAlert = null;
//            }
        }catch (Exception e){
            Alert globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd");
            globalAlert.setHeaderText("Użytkownik nie istnieje");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;
        }

//            Main m = new Main();
//            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//            stage.setScene(m.setMainBoard(stage));

    }


    public void closeWindow(ActionEvent actionEvent) {
        System.exit(0);
    }

}

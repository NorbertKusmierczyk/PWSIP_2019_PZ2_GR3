package desktopApp.controlers;

import desktopApp.LoaderFXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

//@Component
public class CloseWindowController{

    @Autowired
    Controller controller;

    @Autowired
    AnnotationConfigApplicationContext context;

    public void closeWindow() throws Exception{

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.dialogPaneProperty().get();
        alert.setHeaderText("Log out");
        alert.setContentText("Do you want to close program");
        alert.setResizable(false);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            FXMLLoader loader = context.getBean(LoaderFXML.class).getLoader("/LoginFolder/LoginBar.fxml");
            Parent root = loader.load();

            final Scene scene = new Scene(root);
            final Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            scene.getStylesheets().addAll(this.getClass().getResource("/LoginFolder/login.css").toExternalForm());


            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setResizable(false);
            primaryStage.show();
            controller.getMainPane().getScene().getWindow().hide();
        }
    }
}

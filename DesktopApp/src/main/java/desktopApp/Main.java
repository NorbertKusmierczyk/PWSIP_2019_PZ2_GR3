package desktopApp;

import desktopApp.api.IServer;
import desktopApp.config.Config;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Main extends Application {

    double x, y;
    AnnotationConfigApplicationContext context;

    @Override
    public void start(Stage primaryStage) throws Exception{

        context = new AnnotationConfigApplicationContext(Config.class);

        setLoginBoard(primaryStage);

    }

    ////set moveable window
    public void setMoveable(Parent root, final Stage primaryStage){
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
            }
        });
    }

    //@Autowired
    //LoaderFXML loaderFXML;

    public void setLoginBoard(final Stage primaryStage) throws Exception {

        FXMLLoader loader = context.getBean(LoaderFXML.class).getLoader("/LoginFolder/LoginBar.fxml");

        //Parent root = FXMLLoader.load(getClass().getResource("/LoginFolder/LoginBar.fxml"));

        Parent root = loader.load();

        //Parent root = (Parent) loaderFXML.load("/LoginFolder/LoginBar.fxml"

        final Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("/LoginFolder/login.css").toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.show();

        setMoveable(root, primaryStage);
    }




    public static void main(String[] args) {

        //przykladowy kolor #abb0ba #323a47

        launch(args);
    }

}

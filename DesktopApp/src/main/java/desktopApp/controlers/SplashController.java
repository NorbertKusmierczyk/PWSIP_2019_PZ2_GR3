package desktopApp.controlers;

import desktopApp.LoaderFXML;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class SplashController implements Initializable {

    @FXML
    private Circle circ1, circ2;

    @FXML
    private AnchorPane splashMainPane;

    double x, y;
    boolean bigSmallWindow = false;
    Rectangle2D rec;
    Stage stage;

    @Autowired
    AnnotationConfigApplicationContext context;

    public void initialize(URL location, ResourceBundle resources) {

        setRotation(circ1, true, 240, 5);
        setRotation(circ2, true, 180, 4);
        new SplashScreen().start();
    }

    public void setRotation(Circle circle, boolean reverse, int angle, int duration){

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(duration), circle);

        rotateTransition.setAutoReverse(reverse);
        rotateTransition.setByAngle(angle);
        rotateTransition.setDelay(Duration.millis(200));
        rotateTransition.setRate(3);
        rotateTransition.setCycleCount(18);
        rotateTransition.play();
    }

    class SplashScreen extends Thread{
        @Override
        public void run() {

            try {

                Thread.sleep(1000);

                Platform.runLater(new Runnable() {
                    public void run() {
                        Parent root = null;

                        try {
                            //if (context == null)

                            FXMLLoader loader = context.getBean(LoaderFXML.class).getLoader("/sample.fxml");
                            //root = FXMLLoader.load(getClass().getResource("/splash.fxml"));
                            root = loader.load();
                            //root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
                            Scene scene = new Scene(root);
                            stage = new Stage();
                            stage.setScene(scene);
                            stage.initStyle(StageStyle.UNDECORATED);

                            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent event) {
                                    x = event.getSceneX();
                                    y = event.getSceneY();
                                }
                            });

                            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent event) {
                                    stage.setX(event.getScreenX() - x);
                                    stage.setY(event.getScreenY() - y);
                                }
                            });

                            rec = Screen.getPrimary().getVisualBounds();
                            stage.setWidth(rec.getWidth());
                            stage.setHeight(rec.getHeight());
                            stage.setX(1);
                            stage.setY(1);
                            stage.setResizable(true);

                            scene.getStylesheets().addAll(this.getClass().getResource("/style.css").toExternalForm());
                            scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent event) {
                                    if (event.getClickCount() == 2) {
                                        if (!bigSmallWindow) {
                                           stage.setWidth(1200);
                                           stage.setHeight(800);
                                            bigSmallWindow = true;
                                        }
                                        else {
                                            stage.setX(5);
                                            stage.setY(5);
                                            stage.setWidth(rec.getWidth());
                                            stage.setHeight(rec.getHeight());
                                            bigSmallWindow = false;
                                        }
                                    }
                                }
                            });

                            stage.show();
                            splashMainPane.getScene().getWindow().hide();

                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}

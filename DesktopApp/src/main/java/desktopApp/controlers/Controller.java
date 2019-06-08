package desktopApp.controlers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.css.Style;
import desktopApp.MailSender.SendEmail;
import desktopApp.api.IServer;
import desktopApp.config.Config;
import desktopApp.implementation.*;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.text.Position;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Component
public class Controller{



    //private Session


    //kolor tla #558ae0


    @FXML
    AnchorPane mainPane, dashBoardInformation, subUsersContentPane, setPaneGuage1, setPaneGuage2, setPaneGuage3, sentMerchandiseContentPane;

    @FXML
    Label topLabel, dashBoardUserCountLabel, dashBoardProductsCountLabel, dashBoardOrdersCountLabel; // tabs tilted label

   @FXML
   Button availability, manageProducts, sentMessage, sentProducts; // sub menu buttons

   @FXML
    JFXButton orders, dataSet, users, dashBoard, email1, email2; // menu buttons

    @FXML
    JFXTextField usersSearchTextField;

    @FXML
    VBox menuVBox, vbox2, vbox3; // hidden menu bar

    @FXML
    GridPane availabilityContentPane, takeSupplyContentPane, sentMessageContentPane, dataSetContentPane, gridRecipe, recipeProductsGridPane;

    @FXML
    Pane innerGridPaneTiltedPanel, dashBoardGaugePane; // Customer Recipe pane, contain info about seller

    @FXML
    private Pane dashBoardLineChartPane, manegeProductNamePane,manegeProductIDPane,manegeProductPricePane; //manage product wraped panes

    @FXML
    private Label manegeProductIDLabel,manegeProductNameLabel,manegeProductPriceLabel; //manage products wraped panes labels

    @FXML
    ScrollPane ordersContentPane, usersContentPane; //main container of orders tab

    @FXML
    TableView<User> usersTableView;

    @FXML
    TableView ordersTableView;

    @FXML
    TableView<Products> productsTableView;

    @FXML
    TableView<Products> manageProductsTableView;

    @FXML
    TableColumn OrderNumber, Town, TownCode, Street, HouseNumber, ProductID, ProductName, Amount, Price, Name, Surname, OrderEmail, orderDate,/*State*/ Action; // table columns of orders table in orders tab

    @FXML
    TableColumn ID, Username, Email; // users table columns

    @FXML
    TableColumn IDProduct, ProductNameAv, AmountAv;

    @FXML
    private TableColumn manageID,manageProductName,manageAmount,managePrice;


    @FXML
    ListView orderDetailListView; // list of orders tab

    @FXML
    private Label nameSurnameLabel, addressLabel, streetLabel, utterCoast; //label of Cutomer Recipe panel

    @FXML
    private Label userLabelID, userLabelUsername, userLabelEmail;

    @FXML
    private TextField subject_textField;

    @FXML
    private TextArea content_areaField;

    @FXML
    private HBox xBoxMessageFormSet;

    @FXML
    private Label idOldProduct, nameOldProduct, priceOldProduct, idNewProduct, nameNewProduct, priceNewProduct;

    @FXML
    private TextField manageNewID, manageNewName, manageNewPrice;

    private Alert globalAlert;
//

    //@FXML
    //LineChart dashBoardLineChart;

    // tabs topLabel set methods //

    private StringProperty orderHeaderLabel = new SimpleStringProperty("Orders");

    private StringProperty orderHeaderLabelProperty() { return orderHeaderLabel; }

    private void setOrderHeaderLabel(String orderHeaderLabel) { this.orderHeaderLabel.set(orderHeaderLabel); }

    // tabs topLabel set methods END //

    //variable collect rolling vboxes
    ObservableList<VBox> panes = FXCollections.observableArrayList();

    //-----------------------variables responsible for get and gather users orders

    Map<Integer, List<String[]>> productsDetailsMapper;

    Map<Integer, List<String>> personalDetailsMapper;

    Map<Integer, List> ordersGatheredList = new HashMap<Integer, List>();

    Set<Map.Entry<Integer, List>> setWholeOrders;

    ObservableList<Orders> ol = FXCollections.observableArrayList();

    //---------------------variables responsible for get and gather users orders END

    //variable used in usersContentPane to line chart
    Map<Integer, List> userShoppingPeriodMap; //collect information about months when user bought products
    Map<Integer, Integer> amountOfBoughtItems;

    // #1264d1 users list
    ObservableList<User> usersList = FXCollections.observableArrayList();

    // products list
    ObservableList<Products> productsList = FXCollections.observableArrayList();

    // procedure set beginning settings of software
    /*
    *
    *
    *
    *
    *
     */

    private Gauge gauge;
    private Products products;
    private User user;
    List<String> dateStringTab;
    private List<UserOrderDate> dateAndOrderNumberList;
    private List<UserOrderDate> dataSetSaleData;
    private Map<Integer, Map<String, Integer>> idUserMonthsAmount;
    private Set keys;
    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Integer[] digitMonths = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ,11, 12};
    private String[] splitedMonths = null;
    private String singleDate = "";
    private int amountOfItems = 0;
    private LineChart<String, Number> userLineChart;
    private XYChart.Series series2;
    private CategoryAxis xAxis2 = new CategoryAxis();
    private NumberAxis yAxis2 = new NumberAxis();
    private int maxYChart = 0;
    private Map<String, Integer> pastOrders;
    Map<String, Integer> holeSellment;
    int innerValue = 0;

    @FXML
    private JFXTextField availabilityTextField;

    @FXML
    public void initialize() {

        dashBoardUserCountLabel.setText(""+server.countAllUsers());
        dashBoardProductsCountLabel.setText(""+server.countAllProducts());
        dashBoardOrdersCountLabel.setText(""+server.countCurrentOrders());

        gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SPACE_X).maxValue(304)
                .barColor(Color.rgb(46, 68, 140))
                .threshold(290)
                .thresholdColor(Color.rgb(224, 55, 47))
                .barBackgroundColor(Color.rgb(39, 52, 81))
                .prefSize(428,293)
                .animated(true)
                .animationDuration(1000)
                .build();

        dashBoardGaugePane.getChildren().add(gauge);
        dashBoardGaugePane.getChildren().get(dashBoardGaugePane.getChildren().size()-1).setLayoutX(168);
        dashBoardGaugePane.getChildren().get(dashBoardGaugePane.getChildren().size()-1).setLayoutY(65);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    gauge.setValue(server.setDashBoardGauge());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



        setGauge1 = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM)
                .prefSize(285,246)
                .barColor(Color.rgb(57,47,47))
                .valueColor(Color.rgb(57,47,47))
                .barBackgroundColor(Color.rgb(25,120,168))
                .animated(true)
                .animationDuration(600)
                .build();

        setPaneGuage1.getChildren().add(setGauge1);
        setPaneGuage1.getChildren().get(setPaneGuage1.getChildren().size()-1).setLayoutX(25);
        setPaneGuage1.getChildren().get(setPaneGuage1.getChildren().size()-1).setLayoutY(116);

        setGauge2 = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM)
                .prefSize(285,246)
                .barColor(Color.rgb(57,47,47))
                .valueColor(Color.rgb(57,47,47))
                .barBackgroundColor(Color.rgb(224,55,47))
                .animated(true)
                .animationDuration(600)
                .build();

        setPaneGuage2.getChildren().add(setGauge2);
        setPaneGuage2.getChildren().get(setPaneGuage2.getChildren().size()-1).setLayoutX(25);
        setPaneGuage2.getChildren().get(setPaneGuage2.getChildren().size()-1).setLayoutY(116);

        setGauge3 = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM)
                .prefSize(285,246)
                .barColor(Color.rgb(57,47,47))
                .valueColor(Color.rgb(57,47,47))
                .barBackgroundColor(Color.rgb(20,181,18))
                .animated(true)
                .animationDuration(600)
                .build();

        setPaneGuage3.getChildren().add(setGauge3);
        setPaneGuage3.getChildren().get(setPaneGuage3.getChildren().size()-1).setLayoutX(25);
        setPaneGuage3.getChildren().get(setPaneGuage3.getChildren().size()-1).setLayoutY(116);



        // x, y = 0 , 86

        // w, h = 799, 307

        //serwer.getAllUsers();
        dashBoardInformation.toFront();
        topLabel.setText(dashBoard.getText());
        try {
            loadUsers(); //add users to user tab to userTableView and check if connection with database is settled down
        }catch (Exception e){
            globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd");
            globalAlert.setHeaderText("Nie można nawiązać połączenia z bazą danych");
            globalAlert.setResizable(false);
            globalAlert.showAndWait();
            if (globalAlert.getResult() == ButtonType.OK){
                globalAlert = null;
                System.exit(0);
            }
        }
        addProducts(); //add products to warehouse tab to availability to productsTableView
        addManageProducts(); ////add products to warehouse tab to manage products to manageproductsTableView
        initOrderTable(); //initialize orderTableView

        Label title = new Label();
        title.setGraphic(new Label("Sprzedawca  "));
        title.setPadding(new Insets(-10, 10, 0, 20));
        title.getGraphic().setStyle("-fx-background-color: #fff");

        Label idProduct = new Label();
        idProduct.setGraphic(new Label("ID produktu  "));
        idProduct.setPadding(new Insets(-10, 10, 0, 20));
        idProduct.getGraphic().setStyle("-fx-background-color: #fff");

        manegeProductIDPane.getChildren().add(idProduct);
        manegeProductIDPane.setStyle("-fx-border-style: solid inside;");

        Label nameProduct = new Label();
        nameProduct.setGraphic(new Label("Nazwa produktu  "));
        nameProduct.setPadding(new Insets(-10, 10, 0, 20));
        nameProduct.getGraphic().setStyle("-fx-background-color: #fff");

        manegeProductNamePane.getChildren().add(nameProduct);
        manegeProductNamePane.setStyle("-fx-border-style: solid inside;");

        Label priceProduct = new Label();
        priceProduct.setGraphic(new Label("Cena produktu  "));
        priceProduct.setPadding(new Insets(-10, 10, 0, 20));
        priceProduct.getGraphic().setStyle("-fx-background-color: #fff");

        manegeProductPricePane.getChildren().add(priceProduct);
        manegeProductPricePane.setStyle("-fx-border-style: solid inside;");

        manageNewID.setEditable(false);
        manageNewName.setEditable(false);
        manageNewPrice.setEditable(false);

        manageProductsTableView.setOnMouseClicked(event -> {

            products = manageProductsTableView.getSelectionModel().getSelectedItem();
            manegeProductIDLabel.setText(""+products.getId());
            manegeProductNameLabel.setText(products.getProductName());
            manegeProductPriceLabel.setText(""+products.getPrice());

            idOldProduct.setText(""+products.getId());
            nameOldProduct.setText(products.getProductName());
            priceOldProduct.setText(""+products.getPrice());

            manageNewID.setEditable(true);
            manageNewName.setEditable(true);
            manageNewPrice.setEditable(true);
        });

        OldNewProductData oldNewProductData = new OldNewProductData();

        manageNewID.textProperty().bindBidirectional(oldNewProductData.newIDProperty());
        idNewProduct.textProperty().bind(oldNewProductData.newIDProperty());
        manageNewName.textProperty().bindBidirectional(oldNewProductData.newNameProperty());
        nameNewProduct.textProperty().bind(oldNewProductData.newNameProperty());
        manageNewPrice.textProperty().bindBidirectional(oldNewProductData.newPriceProperty());
        priceNewProduct.textProperty().bind(oldNewProductData.newPriceProperty());

        innerGridPaneTiltedPanel.getChildren().add(title);
        innerGridPaneTiltedPanel.setStyle("-fx-border-style: solid inside;");


        usersTableView.setOnMouseClicked(event -> {

            if (subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size()-1) instanceof LineChart || subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size()-1) instanceof Label)
                subUsersContentPane.getChildren().remove(subUsersContentPane.getChildren().size()-1);

            userLineChart = null;
            series2 = null;
            idUserMonthsAmount = null;
            yAxis2 = null;
            xAxis2 = null;
            maxYChart = 0;


            user = usersTableView.getSelectionModel().getSelectedItem();
            userLabelID.setText(""+user.getId());
            userLabelUsername.setText(""+user.getUserName());
            userLabelEmail.setText(""+user.getEmail());
            user = null;

            idUserMonthsAmount = new HashMap<>();
            keys = idUserMonthsAmount.keySet();



            //System.out.println(idUserMonthsAmount.keySet().contains(dateAndOrderNumberList.get(usersTableView.getSelectionModel().getSelectedIndex()).getOrderID()));

            for (UserOrderDate us : dateAndOrderNumberList) {

                if (keys.isEmpty() || !keys.contains(us.getOrderID()))
                    idUserMonthsAmount.put(us.getOrderID(), new HashMap<>());

                singleDate = us.getLocalDate().toString();
                splitedMonths = singleDate.split("-");
                singleDate = splitedMonths[1];
                if (singleDate.indexOf("0") == 0)
                    singleDate.replace('0', '\0');

                for (Integer m : digitMonths) {
                    if (m == Integer.parseInt(singleDate)) {
                        if (idUserMonthsAmount.get(us.getOrderID()).keySet().isEmpty() || !idUserMonthsAmount.get(us.getOrderID()).keySet().contains(months[m - 1]))
                            idUserMonthsAmount.get(us.getOrderID()).put(months[m - 1], us.getIlosc());
                        else {
                            amountOfItems = idUserMonthsAmount.get(us.getOrderID()).get(months[m - 1]);
                            amountOfItems += us.getIlosc();
                            idUserMonthsAmount.get(us.getOrderID()).put(months[m - 1], amountOfItems);
                        }
                    }
                }

            }

            //dziala
//            for (Map.Entry<Integer, Map<String, Integer>> map : idUserMonthsAmount.entrySet()){
//                System.out.println(map.getKey()+"------------------------");
//                for (Map.Entry<String, Integer> subMap : map.getValue().entrySet()){
//                        System.out.println(subMap.getKey()+ "   "+subMap.getValue());
//                }
//            }

            //System.out.println(idUserMonthsAmount.get(usersTableView.getSelectionModel().getSelectedItem().getId()));

            if (idUserMonthsAmount.get(usersTableView.getSelectionModel().getSelectedItem().getId()) != null) {

                series2 = new XYChart.Series<>();
                pastOrders = new LinkedHashMap<>();

                for (Map.Entry<Integer, Map<String, Integer>> map : idUserMonthsAmount.entrySet()) {
                    if (usersTableView.getSelectionModel().getSelectedItem().getId() == map.getKey()) {
                        for (Map.Entry<String, Integer> subMap : map.getValue().entrySet()) {
                            for (int i = 0; i < 12; i++) {
                                if (subMap.getKey().equals(months[i])) {

                                    if (pastOrders.containsKey(subMap.getKey())) {

                                        if (pastOrders.get(subMap.getKey()) < subMap.getValue()) {

                                            pastOrders.put(subMap.getKey(), subMap.getValue());
                                        }
                                    } else {
                                        pastOrders.put(subMap.getKey(), subMap.getValue());
                                    }
                                } else {
                                    if (!pastOrders.containsKey(months[i])) {
                                        pastOrders.put(months[i], 0);
                                    }
                                }
                            }
                        }
                    }
                }


                for (Map.Entry kl : pastOrders.entrySet()) {
                    series2.getData().add(new XYChart.Data<>(kl.getKey(), kl.getValue()));
                }

                ((ObservableList<XYChart.Data>) series2.getData()).forEach(e -> {
                    if ((int) e.getYValue() < maxYChart) ;
                    else maxYChart = (int) e.getYValue();
                });

                xAxis2 = new CategoryAxis();
                yAxis2 = new NumberAxis(0, maxYChart + 1, 1);
                userLineChart = new LineChart<>(xAxis2, yAxis2);

                userLineChart.getData().addAll(series2);
                userLineChart.setLegendVisible(false);
                userLineChart.setPrefSize(812, 239);
                subUsersContentPane.getChildren().add(userLineChart);
                subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1).setLayoutX(805);
                subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1).setLayoutY(610);

            }else{

                if (subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size()-1) instanceof LineChart || subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size()-1) instanceof Label)
                    subUsersContentPane.getChildren().remove(subUsersContentPane.getChildren().size()-1);

                subUsersContentPane.getChildren().add(new Label("Brak danych"));
                ((Label)subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1)).setPrefSize(812,239);
                ((Label)subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1)).setAlignment(Pos.CENTER);
                ((Label)subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1)).setFont(Font.font("Arial", FontWeight.BOLD,45));
                subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1).setLayoutX(805);
                subUsersContentPane.getChildren().get(subUsersContentPane.getChildren().size() - 1).setLayoutY(610);
            }

            singleDate = null;
            splitedMonths = null;

        });



        productsDetailsMapper = new HashMap<>();
        personalDetailsMapper = new HashMap<>();
        panes.add(vbox2);
        panes.add(vbox3);

        userShoppingPeriodMap = new HashMap<>();
        amountOfBoughtItems = new HashMap<>();

        setMostPopularProductList = new ArrayList<>();

        orderDetailListView.setOnMouseClicked(event -> setFormula());


        //find users //
        //*
        // *

        User u = new User();
        usersSearchTextField.textProperty().bindBidirectional(u.searchUsersProperty());

        ObservableList<User> typedUsersTableViewCollections = FXCollections.observableArrayList();
        Comparator<User> userComparator = Comparator.comparing(User::getId);
        usersSearchTextField.setOnKeyTyped(event -> {

            usersList.forEach(users -> {

                if (users.getUserName().contains(usersSearchTextField.getText())) {
                    if (!typedUsersTableViewCollections.contains(users))
                        typedUsersTableViewCollections.add(users);
                }
                else typedUsersTableViewCollections.remove(users);
            });


            FXCollections.sort(typedUsersTableViewCollections, userComparator);
            usersTableView.setItems(typedUsersTableViewCollections);
        });
        //*
        // *
        //find users END //

        ObservableList<Products> typedProductsTableViewCollections = FXCollections.observableArrayList();
        Comparator<Products> productsComparator = Comparator.comparing(Products::getId);

        availabilityTextField.setOnKeyTyped(event -> {

            productsList.forEach(pro -> {

                if (pro.getProductName().contains(availabilityTextField.getText().toUpperCase())) {
                    if (!typedProductsTableViewCollections.contains(pro))
                        typedProductsTableViewCollections.add(pro);
                }
                else typedProductsTableViewCollections.remove(pro);
            });


            FXCollections.sort(typedProductsTableViewCollections, productsComparator);
            productsTableView.setItems(typedProductsTableViewCollections);

        });

        sendProductDataPicker.setValue(LocalDate.now());

        emailList.setOnMouseClicked(event ->{

            // do testow nie usuwac
            setMessage();

        });

        nodes = FXCollections.observableArrayList();
        for (Node n : dataSetGaugesHBox.getChildren()){
            nodes.add(n);
            System.out.println(n);
        }

        dataSetActivityInnerVBox = new VBox();

        emailContentFinder = new LinkedHashMap<>();

    }
    /*
     *
     *
     *
     *
     *
     */
    // procedure set beginning settings of software END

    @Autowired
    SendEmail sendEmail;


    /*
    *
    *  MESSAGES FUNCTIONS CATEGORY
    *
    *
     */
    Scanner scannerRead, scannerWrite;

    File file; //using while reading( sendMessage ) and send mails( sendMesssages )
    StringBuilder sb;

    int idButtonsCounter = 1;
    public void addNewMailForm(){

        getButtonsID();

        if (idMailsButtons.contains("email"+idButtonsCounter)) {
            idButtonsCounter++;
            addNewMailForm();
        }else
            if (xBoxMessageFormSet.getChildren().size() < 6) addNewSubFunction(""+idButtonsCounter);
            else{
                globalAlert = new Alert(Alert.AlertType.ERROR);
                globalAlert.dialogPaneProperty().get();
                globalAlert.setTitle("Błąd");
                globalAlert.setHeaderText("Można dodać nie więcej niż 4 szablony wiadomości");
                globalAlert.setResizable(false);
                globalAlert.show();
                globalAlert = null;
            }
    }

    List<String> idMailsButtons = new ArrayList<>();

    public void getButtonsID(){

        xBoxMessageFormSet.getChildren().forEach(e -> {
            if (e.getId() != null) {
                if (!idMailsButtons.contains(e.getId()))
                idMailsButtons.add(e.getId());
            }
        });
    }

    public void removeButton(String id){

        idButtonsCounter = 1;
        idMailsButtons.remove(id);

    }

    public void addNewSubFunction(String number){
        JFXButton jfxButton = new JFXButton();
        jfxButton.setId("email"+number);
        jfxButton.setPrefSize(135,163);
        jfxButton.setAlignment(Pos.CENTER);
        jfxButton.setPadding(new Insets(0,0,0,2));
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(this.getClass().getResource("/sendMessageTextImage.png").toExternalForm()));
        imageView.setFitWidth(107);
        imageView.setFitHeight(137);
        jfxButton.setGraphic(imageView);
        jfxButton.getStyleClass().add("emailPressed");
        xBoxMessageFormSet.getChildren().add(xBoxMessageFormSet.getChildren().size()-2, jfxButton);
        jfxButton.setOnMouseClicked(event -> sendMessage(event));
    }

    List<File> deletedButtonsFileList = new ArrayList<>();

    public void deleteSelectedScheme(){

        JFXButton tes = null;

        for (Node bt : xBoxMessageFormSet.getChildren()){
            if (xBoxMessageFormSet.getChildren().size() > 3) {
                if (bt.getStyleClass().contains("emailPressedBack")) {
                    tes = (JFXButton) bt;
                    //file = new File(this.getClass().getResource("/style.css").getPath().substring(0, this.getClass().getResource("/style.css").getPath().lastIndexOf("/") + 1) + "/email_version" + bt.getId().substring(bt.getId().length() - 1) + ".txt");
                    file = new File("C:\\TEMP\\email_version" + bt.getId().substring(bt.getId().length() - 1) + ".txt");
                    deletedButtonsFileList.add(file);
                    removeButton(file.getName().substring(file.getName().indexOf(".")-1, file.getName().indexOf(".")));
                    file = null;
            }
            }else{
                globalAlert = new Alert(Alert.AlertType.INFORMATION);
                globalAlert.dialogPaneProperty().get();
                globalAlert.setTitle("Błąd");
                globalAlert.setHeaderText("Musi pozostać przynajmniej jeden szablon");
                globalAlert.setResizable(false);
                globalAlert.show();
                globalAlert = null;
                break;
            }

        }

        xBoxMessageFormSet.getChildren().remove(tes);

        subject_textField.setText("");
        content_areaField.setText("");

    }

    String path = "";

    @FXML
    public void sendMessage(MouseEvent event) {

        // E:\InteliJ\ElectraCodeSystem\target\file:\E:\InteliJ\ElectraCodeSystem\target\ElectraCodeSystem-1.0-SNAPSHOT-jar-with-dependencies.jar!\email_version1.txt
        System.out.println(this.getClass().getResource("/style.css").getPath());

            subject_textField.setText("");
            content_areaField.setText("");
            file = null;
            sb = new StringBuilder();

            for (Node b : xBoxMessageFormSet.getChildren()) {

                if (b.getId() != null) {

                    if (event.getSource() == b) {

                        b.getStyleClass().add("emailPressedBack");

                        try {
                            //file = new File(this.getClass().getResource("/style.css").getPath().substring(0, this.getClass().getResource("/style.css").getPath().lastIndexOf("/") + 1) + "/email_version" + b.getId().substring(b.getId().length() - 1) + ".txt");
                            file = new File("C:\\TEMP\\email_version" + b.getId().substring(b.getId().length() - 1) + ".txt");
                            path = file.getAbsolutePath();
                            if (!file.exists()) file.createNewFile();
                            else {
                                scannerRead = new Scanner(new BufferedReader(new FileReader(file)));
                                if (scannerRead.hasNext()) {
                                    subject_textField.setText(scannerRead.nextLine());
                                    while (scannerRead.hasNext()) {
                                        sb.append(scannerRead.nextLine());
                                        sb.append("\n");
                                    }
                                    content_areaField.setText(sb.toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(b.getId());
                    } else {

                        b.getStyleClass().remove("emailPressedBack");
                    }
                }
            }

        scannerRead.close();
        file = null;
        sb = null;
    }

    public void saveNewEmail(){
        sb = new StringBuilder();
            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter(new File(path)));
                wr.write(subject_textField.getText());
                wr.append("\n");
                wr.append(content_areaField.getText());
                wr.flush();
                wr.close();
                scannerWrite = new Scanner(new BufferedReader(new FileReader(new File(path))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            subject_textField.setText("");
            content_areaField.setText("");
            subject_textField.setText(scannerWrite.nextLine());
            while (scannerWrite.hasNext()) {
                sb.append(scannerWrite.nextLine());
                sb.append("\n");
            }
            content_areaField.setText(sb.toString());
            scannerWrite.close();
            file = null;
            sb = null;

            globalAlert = new Alert(Alert.AlertType.INFORMATION);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Potwierdzenie");
            globalAlert.setHeaderText("Zapisano zmiany");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;

        Label lab = new Label("\t Edytowano wiadomość");
        lab.setPrefSize(dataSetActivityScrollPane.getPrefWidth()-10,50);
        lab.setPadding(new Insets(0,0,0,20));
        Rectangle rec = new Rectangle();
        rec.setHeight(16); rec.setWidth(16); rec.setFill(Paint.valueOf("green"));
        lab.setGraphic(rec);

        dataSetActivityInnerVBox.getChildren().add(lab);
        dataSetActivityInnerVBox.getChildren().add(new Separator());
        dataSetActivityScrollPane.setContent(null);
        dataSetActivityScrollPane.setContent(dataSetActivityInnerVBox);
    }

    boolean lock = true;
    public void setSavedTemplates(){
        if (lock) {
            //File file = new File(this.getClass().getResource("/style.css").getPath().substring(0, this.getClass().getResource("/style.css").getPath().lastIndexOf("/")));
            File file = new File("C:\\TEMP\\");
            File[] nam = file.listFiles();
            for (File b : nam) {
                if (b.getName().contains("email")) {
                    addNewSubFunction(b.getName().substring(b.getName().indexOf(".")-1, b.getName().indexOf(".")));
                }
            }
            file = null;
            nam = null;
            lock = false;
        }
    }

    /*
     *
     *  MESSAGES FUNCTIONS CATEGORY END
     *
     *
     */

    @Autowired
    IServer server;

    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT USERS -- USERS CATEGORY
     *
     *
     */

    public void loadUsers(){

        ID.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        Username.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        Email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        usersList.clear();
        usersList.addAll(server.getAllUsers());
        usersTableView.setItems(usersList);
    }
    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT USERS -- USERS CATEGORY END
     *
     *
     */

    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT ORDERS -- ORDERS CATEGORY-------------------------------
     *
     *
     */

    public void initOrderTable()
    {

        OrderNumber.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("orderNumber"));
        ProductID.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("productID"));
        ProductName.setCellValueFactory(new PropertyValueFactory<Orders, String>("productName"));
        Amount.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("amount"));
        Price.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("price"));
        Name.setCellValueFactory(new PropertyValueFactory<Orders, String>("name"));
        Surname.setCellValueFactory(new PropertyValueFactory<Orders, String>("surname"));
        OrderEmail.setCellValueFactory(new PropertyValueFactory<Orders, String>("email"));
        Town.setCellValueFactory(new PropertyValueFactory<Orders, String>("town"));
        TownCode.setCellValueFactory(new PropertyValueFactory<Orders, String>("townCode"));
        Street.setCellValueFactory(new PropertyValueFactory<Orders, String>("street"));
        HouseNumber.setCellValueFactory(new PropertyValueFactory<Orders, String>("houseNumber"));
        //State.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("state"));
        orderDate.setCellValueFactory(new PropertyValueFactory<Orders, Date>("date"));
        Action.setCellValueFactory(new PropertyValueFactory<Orders, String>("checkBox"));

    }

    public void addRecord(){

        ol.addAll(server.getAllOrders());
        ordersTableView.setItems(ol);
    }

    /*
     *
     *  CHANGE ALL CHECKBOXES TO BE SELECTED
     *
     *
     */

    public void selectAll(){

        for (Orders ord : ol)
            ord.getCheckBox().setSelected(true);

    }

    /*
     *
     *  CHANGE ALL CHECKBOXES TO BE SELECTED END
     *
     *
     */
    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT ORDERS -- ORDERS CATEGORY END--------------------------------
     *
     *
     */


    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT PRODUCTS -- WAREHOUSE - ONLY ID,PRODUCT NAME , AMOUNT
     *
     *
     */

    public void addProducts(){

        IDProduct.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        ProductNameAv.setCellValueFactory(new PropertyValueFactory<Products, String>("productName"));
        AmountAv.setCellValueFactory(new PropertyValueFactory<Products, Integer>("amount"));

        productsList.addAll(server.getAllProducts());

//        AmountAv.setCellFactory(column -> {
//            return new TableCell<Products, Integer>(){
//                @Override
//                protected void updateItem(Integer item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    TableRow table = getTableRow();
//
//                    if (!isEmpty()) {
//                        //setText(""+item);
//                        if (item < 100)
//                       table.setStyle("-fx-background-color: #ed533b");
//                    }
//
//                }
//            };
//
//        });

        productsTableView.setItems(productsList);
    }
    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT PRODUCTS -- WAREHOUSE - ONLY ID,PRODUCT NAME , AMOUNT END
     *
     *
     */


    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT PRODUCTS -- WAREHOUSE - MANIPULATE DATA
     *
     *
     */

    public void addManageProducts(){

        manageID.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        manageProductName.setCellValueFactory(new PropertyValueFactory<Products, String>("productName"));
        manageAmount.setCellValueFactory(new PropertyValueFactory<Products, Integer>("amount"));
        managePrice.setCellValueFactory(new PropertyValueFactory<Products, Integer>("price"));

        manageProductsTableView.setItems(productsList);

        manageProductsTableView.setOnMouseClicked( event -> {

            System.out.println(manageProductsTableView.getSelectionModel().getSelectedItem().getId());
            if (event.getClickCount() == 2){

            }
        });
    }

    public void submitEditProductData(){

        try {
            if (manageNewName.getText().trim().length() <= 0) throw new NumberFormatException();
            int k = Integer.parseInt(manageNewID.getText());
            System.out.println(k);
            int l = Integer.parseInt(manageNewPrice.getText());
            System.out.println(l);


            productsList.forEach(event ->{
                if(event.getId() == k && manageProductsTableView.getSelectionModel().getSelectedItem().getId() != k) throw new IllegalAccessError();
                if (event.getProductName().equals(manageNewName.getText()) && !manageProductsTableView.getSelectionModel().getSelectedItem().getProductName().equals(manageNewName.getText())) throw new IllegalAccessError();

            });

            server.editProduct(k, manageNewName.getText(), l, manageProductsTableView.getSelectionModel().getSelectedItem().getId());


            globalAlert = new Alert(Alert.AlertType.INFORMATION);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Potwierdzenie");
            globalAlert.setHeaderText("Dane edytowano pomyślnie");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;

            manageProductsTableView.getItems().removeAll(productsList);
            manageProductsTableView.getItems().addAll(server.getAllProducts());
            manageNewID.setText("");
            manageNewName.setText("");
            manageNewPrice.setText("");

            Label lab = new Label("\t Edytowano produkt o id: "+k);
            lab.setPrefSize(dataSetActivityScrollPane.getPrefWidth()-10,50);
            lab.setPadding(new Insets(0,0,0,20));
            Rectangle rec = new Rectangle();
            rec.setHeight(16); rec.setWidth(16); rec.setFill(Paint.valueOf("red"));
            lab.setGraphic(rec);

            dataSetActivityInnerVBox.getChildren().add(lab);
            dataSetActivityInnerVBox.getChildren().add(new Separator());
            dataSetActivityScrollPane.setContent(null);
            dataSetActivityScrollPane.setContent(dataSetActivityInnerVBox);


        }catch (NumberFormatException nfe){
            globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd danych");
            globalAlert.setHeaderText("Podano niewłaściwe dane");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;

        }catch (IllegalAccessError iae){
            globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd danych");
            globalAlert.setHeaderText("Produkt o podanym danych już istnieje");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;
        }
    }

    /*
     *
     *  DOWNLOAD DATA FROM DATABASE ABOUT PRODUCTS -- WAREHOUSE - MANIPULATE DATA END
     *
     *
     */



    /*
     *
     *  GENERATED RECIPE FOR CUSTOMERS
     *
     *
     */

    public void generateRecipe(){

        //get all records from usersTableView component (tableView)----

        ObservableList<Orders> toSort = FXCollections.observableArrayList();

        for (Orders ord : ol){
            if (ord.getCheckBox().isSelected()) toSort.add(ord);
        }

        //get all records from usersTableView component (tableView) END---

        SortedList<Orders> recipe = toSort.sorted();

        // collection keys of ordersGatheredList ( Map<Integer, List> )
        Set keys = ordersGatheredList.keySet();

        //mapping orders to suitable list, if key do not exist map create it or add element to accurate list
        List<Orders> list;


        //if table contain only one order
        if (recipe.size() == 1) {
            list = new ArrayList();
            list.add(recipe.get(0));
            ordersGatheredList.put(recipe.get(0).getOrderNumber(), list);
        }
        else{

        for (int i=0; i<recipe.size()-1; i++) {

            if (recipe.get(i).getOrderNumber() == recipe.get(i + 1).getOrderNumber()) {

                list = new ArrayList();
                list.add(recipe.get(i));
                list.add(recipe.get(i + 1));


                if (keys.isEmpty() || !keys.contains(recipe.get(i).getOrderNumber())) {
                    ordersGatheredList.put(recipe.get(i).getOrderNumber(), list);
                } else {
                    ordersGatheredList.get(list.get(0).getOrderNumber()).add(list.get(1));
                }
            } else {

                list = new ArrayList();
                if (i == 0){
                    list.add(recipe.get(i));
                    ordersGatheredList.put(recipe.get(i).getOrderNumber(), list);
                }else {
                    list.add(recipe.get(i + 1));
                    ordersGatheredList.put(recipe.get(i + 1).getOrderNumber(), list);
                }
            }
        }
        }

        //mapping orders to suitable list, if key do not exist map loop create it or add element to exist list END


        //divide orders to separate lists

        List<String> productsDetails = new ArrayList<String>(); //list of products
        String[] buff; //information about single order customer row
        String[] man; //assign content of list - productsDetails
        String[] mainTab; // all rows about customer orders
        boolean setDetailsOnce;
        setWholeOrders = ordersGatheredList.entrySet(); //convert map (Map<Integer, List>) to Set<Map.Entry<Integer, List>> (easier way to get value of key and value under key)

        for (Map.Entry c: setWholeOrders) {

            orderDetailListView.getItems().add("Zamowienie nr "+c.getKey()); //set value of listView
            setDetailsOnce = true; //next customer unlock blockade
            productsDetailsMapper.put((Integer) c.getKey(), new ArrayList<>()); //set new ArrayList<String[]> under loop key
            userShoppingPeriodMap.put((Integer)c.getKey(), new ArrayList()); // set new ArrayList<List> under loop key

            mainTab = c.getValue().toString().split(","); //divide elements of list and assign to string table  = 1/3/2/"vb"/"gh", 1/3/2/"vb"/"gh", 1/3/2/"vb"/"gh"

            for (int i=0; i<mainTab.length; i++) {

                buff = mainTab[i].split("/"); //divide single element of mainTab = 1 3 2 "vb" "gh" elements from 0 to buff.size() - 1

                for (int j=0; j<buff.length; j++){

                    //gather only information about customer from first row then active lock

                    if (setDetailsOnce) {
                        if (j == 5) {
                            personalDetailsMapper.put((Integer)c.getKey(), new ArrayList<String>());
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);
                        }
                        if (j == 6)
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);

                        if (j == 7)
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);

                        if (j == 8)
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);

                        if (j == 9)
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);

                        if (j == 10)
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);

                        if (j == 11){
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);
                            setDetailsOnce = false;
                        }
                    }

                    //gather only information about customer from first row then active lock END

                    //gather information about products  //

                    if (j % 14 == 1)
                        productsDetails.add(buff[j]);
                    if (j % 14 == 2) {
                        productsDetails.add(buff[j]);
                        setMostPopularProductList.add(buff[j]);
                    }
                    if (j % 14 == 3)
                        productsDetails.add(buff[j]);
                    if (j % 14 == 4) {
                        productsDetails.add(buff[j]);
                        man = new String[]{productsDetails.toString()}; //convert list to string[]
                       productsDetailsMapper.get(c.getKey()).add(man);  //add string[] to suitable list - Map<Integer, List<String[]>>
                       productsDetails.removeAll(productsDetails); //clear all list
                    }
                    if (j % 12 == 0){
                        if (buff[j].contains("-")) {
                            if (buff[j].contains("]") || buff[j].contains("[")) {
                                buff[j] = buff[j].replace(']', '\0');
                                buff[j] = buff[j].replace('[', '\0');
                            }
                            userShoppingPeriodMap.get(c.getKey()).add(buff[j]);
                        }

                        //date.add(buff[j]);
                    }

                    //gather information about products END //
                }
            }
            //break;
        }

        //divide orders to separate lists END

        //lopps to watch map collections----------------------------------------------------------

//        for (Map.Entry v : personalDetailsMapper.entrySet())
//            System.out.println(v.getKey()+" = "+v.getValue());
//
//        for (Map.Entry v : productsDetailsMapper.entrySet())
//            System.out.println(v.getKey()+" = "+v.getValue());

//        for (Map.Entry v : userShoppingPeriodMap.entrySet())
//            System.out.println(v.getKey()+" = "+v.getValue());

        //lopps to watch map collections END----------------------------------------------------------


        ol.removeAll(toSort); // clear TableView


        Label l = new Label("\t Pobrano oczekujące zamówienia");
        l.setPrefSize(dataSetActivityScrollPane.getPrefWidth()-10,50);
        l.setPadding(new Insets(0,0,0,20));
        Rectangle rec = new Rectangle();
        rec.setHeight(16); rec.setWidth(16); rec.setFill(Paint.valueOf("blue"));
        l.setGraphic(rec);

        dataSetActivityInnerVBox.getChildren().add(l);
        dataSetActivityInnerVBox.getChildren().add(new Separator());
        dataSetActivityScrollPane.setContent(null);
        dataSetActivityScrollPane.setContent(dataSetActivityInnerVBox);
    }

    /*
     *
     *  GENERATED RECIPE FOR CUSTOMERS END
     *
     *
     */



    /*
     *
     *  SET ALL CUSTOMERS RECIPE FUNCTIONS
     *
     *
     */

    // delete all Labels of GridPane

    public void deleteRows(GridPane gridPane){
        Set<Node> deleteNodes = new HashSet<Node>();
        for (Node child : gridPane.getChildren()) {
            if (!child.toString().contains("VBox"))
            deleteNodes.add(child);
        }
        gridPane.getChildren().removeAll(deleteNodes);
    }

    // delete all Labels of GridPane END

    List<String> l;
    List<String[]> ban;
    int utterCoastInt = 0;
    Label productNumber;
    public void setFormula(){

        utterCoastInt = 0; //count complete coast of order of single customer

        deleteRows(recipeProductsGridPane);

        if (recipeProductsGridPane.getRowConstraints().size() > 1) {
            recipeProductsGridPane.getRowConstraints().remove(1,recipeProductsGridPane.getRowConstraints().size()-1);
        }

        //get element from map Map<Integer, List<String>>
        l = personalDetailsMapper.get(Integer.parseInt(String.valueOf(((String) orderDetailListView.getSelectionModel().getSelectedItem()).substring(14))));

        //set customer details
        nameSurnameLabel.setText(l.get(0)+" "+l.get(1));
        addressLabel.setText(l.get(3)+" "+l.get(4));
        streetLabel.setText(l.get(5)+" "+l.get(6));

        // get element from map Map<Integer, List<String[]>>, there is list only products : idProduct, productName, amount, price
        ban = productsDetailsMapper.get(Integer.parseInt(String.valueOf(orderDetailListView.getSelectionModel().getSelectedItem()).substring(14)));


        String pom = "";
        for (int i=0; i<ban.size(); i++) {

            String[] s1 = ban.get(i); // ban.get return String table from List : [idProduct, productName, amount, price]

            for (int p=0; p<s1.length; p++)
                pom += s1[p];

            pom = pom.replace('[', '\0');
            pom = pom.replace(']', '\0');
            pom.trim();

            String[] js = pom.split(","); //details of single products of customer

            recipeProductsGridPane.getRowConstraints().add(new RowConstraints(65));
            productNumber = new Label(i+1+"");
            productNumber.setPrefWidth(50);
            productNumber.setAlignment(Pos.CENTER);
            recipeProductsGridPane.add(productNumber, 0, i + 1);

            for (int j = 0; j < 4; j++) {
                Label l = new Label(js[j]);
                l.setPrefWidth(recipeProductsGridPane.getColumnConstraints().get(j + 1).getPrefWidth() + 20);
                l.setAlignment(Pos.CENTER);
                recipeProductsGridPane.add(l, j + 1, i + 1);
                if (j == 3)
                    utterCoastInt += Integer.parseInt(l.getText().trim());
                }
                pom = "";
                productNumber = null;
                utterCoast.setText(String.valueOf(utterCoastInt));

        }

        recipeProductsGridPane.getRowConstraints().remove(recipeProductsGridPane.getRowConstraints().size()-1);
    }

    /*
     *
     *  SET ALL CUSTOMERS RECIPE FUNCTIONS END
     *
     *
     */

    public void loadDataFromFile(){

        orderDetailListView.getItems().clear();

        Scanner scanner = null;

        try {

            scanner = new Scanner(new FileReader(new File("cutomersOrders.txt")));


            int markEqualPosition;
            int key;
            String str;
            boolean setDetailsOnce;
            String[] mainTab;
            String[] buff;
            List<String> productsDetails = new ArrayList<String>();
            String[] man;

            while (scanner.hasNext()){

                str = scanner.nextLine();
                markEqualPosition = str.indexOf("=");
                key = Integer.parseInt(str.substring(0,markEqualPosition));

                str = str.replace('[', '\0');
                str = str.replace(']', '\0');
                str.trim();

                String[] ord = str.split(",");
                List<String> l = new ArrayList<>();

                for (int i=0; i<ord.length; i++)
                    l.add(ord[i]);

                Map<Integer, List> map = new HashMap<Integer, List>();
                map.put(key, l);
                setWholeOrders = map.entrySet();

                for (Map.Entry c: setWholeOrders) {

                    orderDetailListView.getItems().add("Order "+c.getKey()); //set value of listView
                    setDetailsOnce = true; //next customer unlock blockade
                    productsDetailsMapper.put((Integer) c.getKey(), new ArrayList<String[]>()); //set new ArrayList<String[]> under loop key

                    mainTab = c.getValue().toString().split(","); //divide elements of list and assign to string table  = 1/3/2/"vb"/"gh", 1/3/2/"vb"/"gh", 1/3/2/"vb"/"gh"

                    for (int i=0; i<mainTab.length; i++) {

                        buff = mainTab[i].split("/"); //divide single element of mainTab = 1 3 2 "vb" "gh" elements from 0 do buff.size() - 1

                        for (int j=0; j<buff.length; j++){

                            //gather only information about customer from first row then active lock

                            if (setDetailsOnce) {
                                if (j == 5) {
                                    personalDetailsMapper.put((Integer)c.getKey(), new ArrayList<String>());
                                    personalDetailsMapper.get(c.getKey()).add(buff[j]);
                                }
                                if (j == 6)
                                    personalDetailsMapper.get(c.getKey()).add(buff[j]);
                                if (j == 7) {
                                    personalDetailsMapper.get(c.getKey()).add(buff[j]);
                                    setDetailsOnce = false;
                                }

                            }

                            //gather only information about customer from first row then active lock END

                            //gather information about products  //

                            if (j % 9 == 1)
                                productsDetails.add(buff[j]);
                            if (j % 9 == 2)
                                productsDetails.add(buff[j]);
                            if (j % 9 == 3)
                                productsDetails.add(buff[j]);
                            if (j % 9 == 4) {
                                productsDetails.add(buff[j]);
                                man = new String[]{productsDetails.toString()}; //convert list to string[]
                                productsDetailsMapper.get(c.getKey()).add(man);  //add string[] to suitable list - Map<Integer, List<String[]>>
                                productsDetails.removeAll(productsDetails); //clear all list
                            }

                            //gather information about products END //
                        }
                    }
                    //break;
                }
                //ordersGatheredList.put(Integer.parseInt(str.substring(0,markEqualPosition)), new)
            }

        }catch (Exception e){

        }

        scanner.close();

    }

    public void writeDataToFile() throws Exception{

        StringBuilder l = new StringBuilder();
        File f = new File("cutomersOrders.txt");
        for (Map.Entry entry : setWholeOrders) {
            l.append(entry.getKey()+"="+entry.getValue()+"\n");
        }

        BufferedWriter bw = new BufferedWriter(new PrintWriter(f.getAbsoluteFile()));
        bw.append(l.toString());
        bw.close();

    }

    // *
    // *
    // *
    //set all Customer Recipe site END


    /*
     *
     *  SEND MAILS TO CUSTOMERS CATEGORY FUNCTIONS
     *
     *
     */

    Map<Integer, String> emailMapper;
    @FXML
    JFXDatePicker sendProductDataPicker;
    @FXML
    ComboBox<String> chooseMailComboBox;
    @FXML
    Label sendProductsMessageLabel;
    @FXML
    TableView<EmailList> emailList;
    @FXML
    TableColumn Mail, Supply, mailTemplate;
    ObservableList<String> idSelectedEmailList;
    ObservableList<EmailList> emailListObservableList;
    Map<Integer,String> set;
    public void sendProductsCategory(){

        emailMapper = null;
        emailMapper = new LinkedHashMap<>();
        int tw;

        idSelectedEmailList = null;
        emailListObservableList = null;
        emailList.getItems().clear();
        chooseMailComboBox.getItems().clear();
        emailListObservableList = FXCollections.observableArrayList();
        idSelectedEmailList = FXCollections.observableArrayList();


        Mail.setCellValueFactory(new PropertyValueFactory<EmailList, String>("idUserMailUser"));
        Supply.setCellValueFactory(new PropertyValueFactory<EmailList, String>("supply"));
        mailTemplate.setCellValueFactory(new PropertyValueFactory<EmailList, String>("comboBox"));

        Iterator<Integer> itr = personalDetailsMapper.keySet().iterator();
        while (itr.hasNext()){
            tw = itr.next();
            emailMapper.put(tw, personalDetailsMapper.get(tw).get(2));
        }

        for (Node id : xBoxMessageFormSet.getChildren()){
            if (id.getId() != null) idSelectedEmailList.add(id.getId());
        }

        set = new LinkedHashMap<>();
        dateAndOrderNumberList.forEach(e -> {
            set.put(e.getOrderID(), e.getSupply());
        });

        set.forEach((e1,e2)-> System.out.println(e1+"   "+e2));

        emailMapper.forEach((e,f) -> {
            emailListObservableList.add(new EmailList(e+"/"+f, set.get(e)));
        });

        chooseMailComboBox.getItems().add("brak");
        chooseMailComboBox.getItems().addAll(idSelectedEmailList);
        chooseMailComboBox.getSelectionModel().selectFirst();

        emailListObservableList.forEach(e -> {e.getComboBox().getItems().addAll(idSelectedEmailList); e.getComboBox().getSelectionModel().selectFirst(); });
        emailList.getItems().addAll(emailListObservableList);
    }


    public void changeDate(){
        sendProductDataPicker.setValue(sendProductDataPicker.getValue());
        setMessage();
    }

    public void setMessage(){

        sb = null;
        System.out.println(emailList.getSelectionModel().getSelectedItem().getComboBox().getSelectionModel().getSelectedItem());
        //file = new File(this.getClass().getResource("/style.css").getPath().substring(0, this.getClass().getResource("/style.css").getPath().lastIndexOf(
          //      "/") + 1) + "/email_version"+ emailList.getSelectionModel().getSelectedItem().getComboBox().getSelectionModel().getSelectedItem().toString().substring(5)+
            //    ".txt");
        file = new File("C:\\TEMP\\email_version" + emailList.getSelectionModel().getSelectedItem().getComboBox().getSelectionModel().getSelectedItem().toString().
                substring(5)+ ".txt");
        try {
            scannerRead = new Scanner(new BufferedReader(new FileReader(file)));
            sb = new StringBuilder();
            while(scannerRead.hasNext()) {
                sb.append(scannerRead.nextLine());
                sb.append("\n\n");
            }
            System.out.println(sb.toString());
            sendProductsMessageLabel.setAlignment(Pos.TOP_LEFT);
            sendProductsMessageLabel.setFont(Font.font("Arial",FontWeight.NORMAL, 20));
            sendProductsMessageLabel.setPadding(new Insets(0,0,0,20));
            if (sb.toString().contains("*") || sb.toString().contains("-") || sb.toString().contains("/"))
                sb.replace(sb.indexOf("*"), sb.indexOf("*")+1, ""+emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().substring(0,emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().indexOf("/")));

            List<String> list = personalDetailsMapper.get(Integer.parseInt(emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().substring(0,emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().indexOf("/"))));
            sb.append("Dzień wysłania towaru: "+sendProductDataPicker.getValue()+", sposob dostawy: "+emailList.getSelectionModel().getSelectedItem().getSupply()+",\n"+
                    "na adres: "+list.get(3)+" "+list.get(4)+" "+list.get(5)+" "+list.get(6));
            sendProductsMessageLabel.setText(sb.toString());
            scannerRead.close();
            scannerRead = null;
            //sb = null;

        }catch (Exception e){
            globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd");
            globalAlert.setHeaderText("Nie można odczytać wiadomości");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;
        }
    }

    public void setDisableSetEnable(){
        System.out.println(chooseMailComboBox.getSelectionModel().getSelectedItem());
        if (!chooseMailComboBox.getSelectionModel().getSelectedItem().equals("brak")) {
            emailList.getItems().forEach(e -> e.getComboBox().getSelectionModel().select(chooseMailComboBox.getSelectionModel().getSelectedItem()));
            emailListObservableList.forEach(e -> e.getComboBox().setDisable(true));
        }else{
            emailListObservableList.forEach(e -> e.getComboBox().setDisable(false));
        }
    }



    Map<String, String> emailContentFinder;
    String[] header;
    String content;
    public void sendMessages(){

        ObservableList<EmailList> ol = emailList.getItems();

        for (EmailList l : ol){
            emailList.getSelectionModel().select(l);
            setMessage();
            emailContentFinder.put(emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().substring(
                    emailList.getSelectionModel().getSelectedItem().getIdUserMailUser().indexOf("/")+1), sb.toString());
        }

        try {
            emailContentFinder.forEach((e, e2) -> {

                header = emailContentFinder.get(e).split("\n\n");
                content = emailContentFinder.get(e).replaceFirst(header[0], "");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendEmail.sendEmail(e, header[0], content);
                    }
                }).start();
                try {
                    Thread.sleep(1000);
                }catch (Exception e9){
                    e9.printStackTrace();
                }
            });

        }catch (Exception e){
            globalAlert = new Alert(Alert.AlertType.ERROR);
            globalAlert.dialogPaneProperty().get();
            globalAlert.setTitle("Błąd");
            globalAlert.setHeaderText("Nie można wysłać wiadomości");
            globalAlert.setResizable(false);
            globalAlert.show();
            globalAlert = null;
        }

        Label lab = new Label("\t Wysłano maile do użytkowników");
        lab.setPrefSize(dataSetActivityScrollPane.getPrefWidth()-10,50);
        lab.setPadding(new Insets(0,0,0,20));
        Rectangle rec = new Rectangle();
        rec.setHeight(16); rec.setWidth(16); rec.setFill(Paint.valueOf("blue"));
        lab.setGraphic(rec);

        dataSetActivityInnerVBox.getChildren().add(lab);
        dataSetActivityInnerVBox.getChildren().add(new Separator());
        dataSetActivityScrollPane.setContent(null);
        dataSetActivityScrollPane.setContent(dataSetActivityInnerVBox);

    }

    /*
     *
     *  SEND MAILS TO CUSTOMERS CATEGORY FUNCTIONS END
     *
     *
     */


    /*
     *
     *  CREATE DATA SET CATEGORY FUNCTIONS
     *
     *
     */

    @FXML
    private Label dataSetMainLabel, gauge1Label, gauge2Label, gauge3Label;
    @FXML
    ScrollPane dataSetActivityScrollPane;
    VBox dataSetActivityInnerVBox = new VBox();;

    private int amountOfRegisteredUsers = 0;
    private LineChart<String, Number> dataSetLineChart;
    private XYChart.Series dataSetSeries;
    private CategoryAxis xAxisDataSet = new CategoryAxis();
    private NumberAxis yAxis2DataSet = new NumberAxis();
    private int maxYChartDataSet = 0;
    private double averageAmountOfRegisteredUsers = 0;
    private Map<String, Integer> allRegisteredUsers;
    private double lastRegistered = 0;
    private Gauge setGauge1, setGauge2, setGauge3;
    private boolean dataSetLock = false;
    Timeline task;


    public void setGuagesToZero(){
        gauge.setValue(0);
        setGauge1.setValue(0);
        setGauge2.setValue(0);
        setGauge3.setValue(0);
    }

    public void dataSetRegisterUsersFunction(){

        if (dataSetGaugesHBox.getChildren().get(0).getId() == null){
               dataSetGaugesHBox.getChildren().remove(0);
               dataSetGaugesHBox.getChildren().addAll(nodes);
        }



        if (dataSetLock) {

            //task = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(dataSetIndicator.progressProperty(), 0)), new KeyFrame(Duration.seconds(1), new KeyValue(dataSetIndicator.progressProperty(), 1)));
            //task.playFromStart();
            setGuagesToZero();
          try {
            Thread.sleep(600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        dataSetLock = true;

        dataSetMainLabel.setText("Użytkownicy");
        dataSetMainLabel.setStyle("-fx-background-color:  #6192da");

        gauge1Label.setText("Ostatnio zarejestrowani");
        gauge2Label.setText("Wszystkich zarejestrowanych");
        gauge3Label.setText("Średnia rejestracji");


        allRegisteredUsers = null;
        dataSetSeries = null;
        singleDate = null;
        splitedMonths = null;
        maxYChartDataSet = 0;
        averageAmountOfRegisteredUsers = 0;

        if (dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size()-1) instanceof LineChart || dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size()-1) instanceof PieChart)
            dataSetContentPane.getChildren().remove(dataSetContentPane.getChildren().size()-1);

        averageAmountOfRegisteredUsers = 0;
        allRegisteredUsers = new LinkedHashMap<>();
        dataSetSeries = new XYChart.Series<>();
        for (String k : months) allRegisteredUsers.put(k, 0);

        usersList.forEach(user -> {
//
                    singleDate = user.getDate().toString();
                    splitedMonths = singleDate.split("-");
                    singleDate = splitedMonths[1];
                    if (singleDate.indexOf("0") == 0)
                        singleDate.replace('0', '\0');

                    for (Integer m : digitMonths) {
                        if (m == Integer.parseInt(singleDate)) {

                            amountOfRegisteredUsers = allRegisteredUsers.get(months[m-1]);
                            amountOfRegisteredUsers++;
                            allRegisteredUsers.put(months[m-1], amountOfRegisteredUsers);
                        }
                    }
        });

        for (Map.Entry kl : allRegisteredUsers.entrySet()) {
            dataSetSeries.getData().add(new XYChart.Data<>(kl.getKey(), kl.getValue()));
        }

        ((ObservableList<XYChart.Data>) dataSetSeries.getData()).forEach(e -> {
            if ((int) e.getYValue() < maxYChartDataSet) ;
            else maxYChartDataSet = (int) e.getYValue();
            averageAmountOfRegisteredUsers += (int) e.getYValue();
        });

        System.out.println(maxYChartDataSet);

        xAxisDataSet = new CategoryAxis();
        yAxis2DataSet = new NumberAxis(0, maxYChartDataSet + 1, 1);
        dataSetLineChart = new LineChart<>(xAxisDataSet, yAxis2DataSet);

        dataSetLineChart.getData().addAll(dataSetSeries);
        dataSetLineChart.setLegendVisible(false);
        dataSetContentPane.add(dataSetLineChart,0,1);


        lastRegistered = 0;
        allRegisteredUsers.forEach((ev, tw) ->{
            if (ev.toLowerCase().equals(LocalDate.now().getMonth().toString().toLowerCase().substring(0, 3))) lastRegistered = tw;
        });

        setGauge1.setMaxValue(maxYChartDataSet+1);
        setGauge1.setValue(lastRegistered);

        setGauge2.setMaxValue(server.getAllUsers().size());
        setGauge2.setValue(setGauge2.getMaxValue());

        setGauge3.setMaxValue(100);
        setGauge3.setValue((averageAmountOfRegisteredUsers/12)*100);

    }

    public void dataSetAverageSellmentFunction(){

        setGuagesToZero();

        if (dataSetGaugesHBox.getChildren().get(0).getId() == null){
            dataSetGaugesHBox.getChildren().remove(0);
            dataSetGaugesHBox.getChildren().addAll(nodes);
        }

        try {
            Thread.sleep(600);
        } catch (Exception e) {
            e.printStackTrace();
        }


        dataSetMainLabel.setText("Sprzedaż");
        dataSetMainLabel.setStyle("-fx-background-color:  #f1413e");

        gauge1Label.setText("Ostatnio sprzedano");
        gauge2Label.setText("Wszystkich sprzedaży");
        gauge3Label.setText("Średnia sprzedaży");


        dataSetSeries = null;

        if (dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size()-1) instanceof LineChart || dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size()-1) instanceof PieChart)
            dataSetContentPane.getChildren().remove(dataSetContentPane.getChildren().size()-1);


        maxYChartDataSet = 0;
        averageAmountOfRegisteredUsers = 0;
        dataSetSeries = new XYChart.Series<>();

        setSaleLineChart();

//        for (Map.Entry<String, Integer> map : holeSellment.entrySet())
//            System.out.println(map.getKey()+"   "+map.getValue());

        for (Map.Entry kl : holeSellment.entrySet()) {
            dataSetSeries.getData().add(new XYChart.Data<>(kl.getKey(), kl.getValue()));
        }

        ((ObservableList<XYChart.Data>) dataSetSeries.getData()).forEach(e -> {
            if ((int) e.getYValue() < maxYChartDataSet) ;
            else maxYChartDataSet = (int) e.getYValue();
            averageAmountOfRegisteredUsers += (int) e.getYValue();
        });

        xAxisDataSet = new CategoryAxis();
        yAxis2DataSet = new NumberAxis(0, maxYChartDataSet + 1, 1);
        dataSetLineChart = new LineChart<>(xAxisDataSet, yAxis2DataSet);

        dataSetLineChart.getData().addAll(dataSetSeries);
        dataSetLineChart.setLegendVisible(false);
        dataSetContentPane.add(dataSetLineChart,0,1);

        setGauge1.setMaxValue(maxYChartDataSet+1);
        setGauge1.setValue(lastRegistered);

        setGauge2.setMaxValue(server.getAllOrdersDate().size());
        setGauge2.setValue(setGauge2.getMaxValue());

        setGauge3.setMaxValue(100);
        setGauge3.setValue((averageAmountOfRegisteredUsers/304)*100);


        holeSellment = null;
    }

    public void setSaleLineChart(){

        idUserMonthsAmount = new HashMap<>();
        keys = idUserMonthsAmount.keySet();
        holeSellment = new LinkedHashMap<>();

        for (UserOrderDate us : dataSetSaleData) {

            if (keys.isEmpty() || !keys.contains(us.getOrderID()))
                idUserMonthsAmount.put(us.getOrderID(), new HashMap<>());

            singleDate = us.getLocalDate().toString();
            splitedMonths = singleDate.split("-");
            singleDate = splitedMonths[1];
            if (singleDate.indexOf("0") == 0)
                singleDate.replace('0', '\0');

            for (Integer m : digitMonths) {
                if (m == Integer.parseInt(singleDate)) {
                    if (idUserMonthsAmount.get(us.getOrderID()).keySet().isEmpty() || !idUserMonthsAmount.get(us.getOrderID()).keySet().contains(months[m - 1]))
                        idUserMonthsAmount.get(us.getOrderID()).put(months[m - 1], us.getIlosc());
                    else {
                        amountOfItems = idUserMonthsAmount.get(us.getOrderID()).get(months[m - 1]);
                        amountOfItems += us.getIlosc();
                        idUserMonthsAmount.get(us.getOrderID()).put(months[m - 1], amountOfItems);
                    }
                }
            }

        }

        for (String mon : months) if (!holeSellment.containsKey(mon)) holeSellment.put(mon, 0);

        for (Map.Entry<Integer, Map<String, Integer>> map : idUserMonthsAmount.entrySet()){
            System.out.println(map.getKey()+"------------------------");
            for (Map.Entry<String, Integer> subMap : map.getValue().entrySet()){


                if (!holeSellment.keySet().contains(subMap.getKey()))
                    holeSellment.put(subMap.getKey(), subMap.getValue());
                else{
                    innerValue = holeSellment.get(subMap.getKey());
                    innerValue += subMap.getValue();
                    holeSellment.put(subMap.getKey(), innerValue);
                }
            }
        }

        holeSellment.forEach((ev, tw) ->{
            if (ev.toLowerCase().equals(LocalDate.now().getMonth().toString().toLowerCase().substring(0, 3))) lastRegistered = tw;
        });

        idUserMonthsAmount = null;
        keys = null;
    }

    private List<String> setMostPopularProductList;
    private Map<String, Integer> popularProductsMap= new HashMap<>();
    int am = 0;
    @FXML
    HBox dataSetGaugesHBox;
    private ObservableList<Node> nodes;
    private Label mostPopularProductLabelHeader, mostPopularProductLabelContent;
    private VBox innerVBox;
    int hyperx = 1;
    private List<String> mostPopularProductList;
    public void dataSetPopularProductsFunction() throws Exception{ //dodac kolumne dostaw

        dataSetMainLabel.setText("Produkty");
        dataSetMainLabel.setStyle("-fx-background-color:   #14b512");

        setGuagesToZero();

        setMostPopularProductList.forEach(e->{
            if (!popularProductsMap.containsKey(e))
                popularProductsMap.put(e, 1);
            else {
                am = popularProductsMap.get(e);
                am++;
                popularProductsMap.put(e, am);
            }


        });

        popularProductsMap.forEach((e,e2)->System.out.println(e+"   "+e2));

        popularProductsMap.forEach((e,e2)->{
            if (e2 > hyperx){
                hyperx = e2;
            }
        });

        mostPopularProductList = new ArrayList<>();

        for (Map.Entry<String, Integer> map : popularProductsMap.entrySet()){
            if (map.getValue() == hyperx) mostPopularProductList.add(map.getKey());
        }

        dataSetGaugesHBox.getChildren().removeAll(nodes);

        mostPopularProductLabelHeader = new Label();
        mostPopularProductLabelHeader.setAlignment(Pos.CENTER);
        mostPopularProductLabelHeader.setPadding(new Insets(40,0,40,0));
        mostPopularProductLabelHeader.setPrefSize(dataSetGaugesHBox.getPrefWidth(), 80);
        mostPopularProductLabelHeader.setFont(Font.font("Arial", FontWeight.NORMAL, 26));
        mostPopularProductLabelHeader.textFillProperty().setValue(Paint.valueOf("#fff"));

        if (mostPopularProductList.size() > 1) mostPopularProductLabelHeader.setText("Najpopularniejsze przedmioty to:");
        else mostPopularProductLabelHeader.setText("Najpopularniejszy przedmiot to:");

        mostPopularProductLabelContent = new Label();
        mostPopularProductLabelContent.setAlignment(Pos.CENTER);
        mostPopularProductLabelContent.setPrefWidth(dataSetGaugesHBox.getPrefWidth());
        mostPopularProductLabelContent.setFont(Font.font("Arial", FontWeight.NORMAL, 26));
        mostPopularProductLabelContent.textFillProperty().setValue(Paint.valueOf("#fff"));

        StringBuilder sb = new StringBuilder();
        for (String str : mostPopularProductList){
            sb.append(str+"\n\n");
        }

        mostPopularProductLabelContent.setText(sb.toString());



        innerVBox = new VBox();
        innerVBox.setStyle("-fx-background-color: #24cc5c;");
        innerVBox.getChildren().add(mostPopularProductLabelHeader);
        innerVBox.getChildren().add(mostPopularProductLabelContent);


        dataSetGaugesHBox.getChildren().add(innerVBox);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        popularProductsMap.forEach((e,e2)->{
            pieChartData.add(new PieChart.Data(e,e2));
        });

        if (dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size() - 1) instanceof LineChart || dataSetContentPane.getChildren().get(dataSetContentPane.getChildren().size() - 1) instanceof PieChart)
            dataSetContentPane.getChildren().remove(dataSetContentPane.getChildren().size() - 1);
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendVisible(false);
        dataSetContentPane.add(chart, 0, 1);

       // pieChartData.clear();

        dateAndOrderNumberList.forEach(ev->System.out.println(ev.getOrderID()+" "+ev.getSupply()+"  "+ev.getLocalDate()));
    }

    /*
     *
     *  CREATE DATA SET CATEGORY FUNCTIONS
     *
     *
     */




    /*
     *
     *  SOFTWARE BASIC FUNCTIONS -----------------------------------------------------------
     *
     *
     */
    boolean click = true;
    public void hideLists()
    {
        for (VBox boxes : panes) {
            boxes.setVisible(false);
            boxes.setPrefHeight(0);
        }
    }

    public void co(){
        minimazeList(vbox2);
    }
    public void sy(){
        minimazeList(vbox3);
    }

    private void minimazeList(VBox vbox){

        for (VBox p : panes){
            if (p.equals(vbox)) {
                if (click) {
                    p.setVisible(true);
                    p.setPrefHeight(100);
                    p.setPrefWidth(200);
                    click = false;
                }else{
                    p.setVisible(false);
                    p.setPrefHeight(0);
                    click = true;
                }
            } else {
                p.setVisible(false);
                p.setPrefHeight(0);
            }
        }
    }
    // hide roll down sub bar lists  END


        /*
         *
         *  SWITCHING BETWEEN MENU CATEGORIES
         *
         *
         */
    public ArrayList<Node> getAllNodes(VBox root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllNodes(root, nodes);
        return nodes;
    }


    //get all sub nodes of components
    public void addAllNodes(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Button || node instanceof JFXButton)
            nodes.add(node);
            if (node instanceof Parent)
              addAllNodes((Parent)node, nodes);
        }
    }

    //ArrayList<Node> m = getAllNodes(menuVBox);
    public void changeFocusedButtonBackgroundColor(Object object) {

        for (Node node : getAllNodes(menuVBox)) {
            if (node == object) {
                node.getStyleClass().clear();
                node.getStyleClass().add("selectedButton");
            } else {
                node.getStyleClass().clear();
                node.getStyleClass().add("subMenuHover");
            }
        }
    }


    @FXML
    private void handleAction(MouseEvent event){

        changeFocusedButtonBackgroundColor(event.getSource());
        setGuagesToZero();
        dataSetLock = false; // set lock at gauges in data set category (first visit does not demand setting gauges in new threads )


        /*
         *
         *  DELETING MESSAGES FROM MESSAGES TEMPLATES
         *
         *
         */
        if (!deletedButtonsFileList.isEmpty()) {
            for (Iterator<File> it = deletedButtonsFileList.iterator(); it.hasNext(); ) {
                File next = it.next();
                System.out.println(next.getAbsolutePath());
                //if (next.exists()) {
                    next.delete();
                //}
                it.remove();
            }
        }
        /*
         *
         *  DELETING MESSAGES FROM MESSAGES END
         *
         *
         */


        manageNewID.setEditable(false);
        manageNewName.setEditable(false);
        manageNewPrice.setEditable(false);
        manageNewID.setText("");
        manageNewName.setText("");
        manageNewPrice.setText("");
        manegeProductIDLabel.setText("");
        manegeProductNameLabel.setText("");
        manegeProductPriceLabel.setText("");
        idOldProduct.setText("");
        nameOldProduct.setText("");
        priceOldProduct.setText("");
        manageProductsTableView.getSelectionModel().clearSelection();

        if (event.getSource() == orders){
            setOrderHeaderLabel(orders.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            ordersContentPane.toFront();
            hideLists();
            click = true;

        }

        if (event.getSource() == availability) {
            setOrderHeaderLabel(availability.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            availabilityContentPane.toFront();
        }
        if (event.getSource() == manageProducts) {
            setOrderHeaderLabel(manageProducts.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            takeSupplyContentPane.toFront();
            manageProductsTableView.getSelectionModel().selectFirst();
        }

        if (event.getSource() == sentMessage){
            setOrderHeaderLabel(sentMessage.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            sentMessageContentPane.toFront();
            subject_textField.setText("");
            content_areaField.setText("");
            xBoxMessageFormSet.getChildren().forEach(node -> node.getStyleClass().remove("emailPressedBack") );
            setSavedTemplates();
        }

        if (event.getSource() == sentProducts){
            setOrderHeaderLabel(sentProducts.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            setSavedTemplates();
            dateAndOrderNumberList = server.getAllOrdersDate();
            sendProductsCategory();
            sendProductsMessageLabel.setText("Wiadomość");
            sentMerchandiseContentPane.toFront();
        }

        if (event.getSource() == dataSet) {
            setOrderHeaderLabel(dataSet.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            hideLists();
            click = true;
            dateAndOrderNumberList = server.getAllOrdersDate();
            dataSetContentPane.toFront();
            dataSetRegisterUsersFunction();
            dataSetSaleData = server.getAllOrdersDate();
        }

        if (event.getSource() == users){
            setOrderHeaderLabel(users.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            //usersContentPane.toFront();
            subUsersContentPane.toFront();
            dateAndOrderNumberList = server.getAllOrdersDate();
            loadUsers();
            hideLists();
            click = true;
        }

        if (event.getSource() == dashBoard){
            setOrderHeaderLabel(dashBoard.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            hideLists();
            click = true;
            gauge.setValue(server.setDashBoardGauge());
            dashBoardInformation.toFront();
        }
    }
    /*
     *
     *  SWITCHING BETWEEN MENU CATEGORIES END
     *
     *
     */

    /*
     *
     *  SOFTWARE BASIC FUNCTIONS -----------------------------------------------------------
     *
     *
     */


    // procedure close all program


    @Autowired
    CloseWindowController closeWindowController;

    public AnchorPane getMainPane(){ return mainPane;}

    public void closeWindow() throws Exception{
        closeWindowController.closeWindow();
    }

    // procedure close all program END


}

package desktopApp.controlers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.css.Style;
import desktopApp.MailSender.SendEmail;
import desktopApp.api.IServer;
import desktopApp.config.Config;
import desktopApp.implementation.Orders;
import desktopApp.implementation.Products;
import desktopApp.implementation.User;
import desktopApp.implementation.UserOrderDate;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.SpaceXSkin;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class Controller{



    //private Session


    //kolor tla #558ae0

    @FXML
    ProgressIndicator prog;

    @FXML
    AnchorPane mainPane, dashBoardInformation, subUsersContentPane;

    @FXML
    Label topLabel; // tabs tilted label

   @FXML
   Button availability, manageProducts, sentMessage, sentProducts; // sub menu buttons

   @FXML
    JFXButton orders, dataSet, users, dashBoard, email1, email2; // menu buttons

    @FXML
    JFXTextField usersSearchTextField;

    @FXML
    VBox menuVBox, vbox2, vbox3; // hidden menu bar

    @FXML
    GridPane availabilityContentPane, takeSupplyContentPane, sentMessageContentPane, sentMerchandiseContentPane, dataSetContentPane, gridRecipe, recipeProductsGridPane;

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
    TableView ordersTableView, productsTableView;

    @FXML
    TableView<Products> manageProductsTableView;

    @FXML
    TableColumn OrderNumber, Town, TownCode, Street, HouseNumber, ProductID, ProductName, Amount, Price, Name, Surname, orderDate,/*State*/ Action; // table columns of orders table in orders tab

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


    //@FXML
    //LineChart dashBoardLineChart;

    // tabs topLabel set methods //

    StringProperty orderHeaderLabel = new SimpleStringProperty("Orders");

    public StringProperty orderHeaderLabelProperty() { return orderHeaderLabel; }

    public void setOrderHeaderLabel(String orderHeaderLabel) { this.orderHeaderLabel.set(orderHeaderLabel); }

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

    @Autowired
    SendEmail sendEmail;
    Scanner scanner;
    File file;

    public void addNewMailForm(){
        JFXButton jfxButton = new JFXButton();
        jfxButton.setId("email"+xBoxMessageFormSet.getChildren().size());
        jfxButton.setPrefSize(135,163);
        jfxButton.setAlignment(Pos.CENTER);
        jfxButton.setPadding(new Insets(0,0,0,2));
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("file:\\E:\\InteliJ\\ElectraCodeSystem\\src\\main\\resources\\sendMessageTextImage.png"));
        imageView.setFitWidth(107);
        imageView.setFitHeight(137);
        jfxButton.setGraphic(imageView);
        jfxButton.getStyleClass().add("emailPressed");
        xBoxMessageFormSet.getChildren().add(xBoxMessageFormSet.getChildren().size()-1, jfxButton);
        jfxButton.setOnMouseClicked(event -> sendMessage(event));

    }

    @FXML
    public void sendMessage(MouseEvent event) {

        subject_textField.setText("");
        content_areaField.setText("");
        scanner = null;
        file = null;

        for (Node b : xBoxMessageFormSet.getChildren()) {

            if (b.getId() != null) {

                if (event.getSource() == b) {

                    b.getStyleClass().add("emailPressedBack");
                    System.out.println(event.getSource());
                    try {
                        file = new File("E:\\InteliJ\\ElectraCodeSystem\\email_version" + b.getId().substring(b.getId().length() - 1) + ".txt");
                        if (!file.exists()) file.createNewFile();
                        else {
                            scanner = new Scanner(new BufferedReader(new FileReader(file)));
                            if (scanner.hasNext()) {
                                subject_textField.setText(scanner.nextLine());
                                while (scanner.hasNext()) {
                                    content_areaField.setText(scanner.nextLine());
                                }
                                scanner = null;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("usuwam klase przyciskom "+b.getId());
                    b.getStyleClass().remove("emailPressedBack");
                }
            }
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //sendEmail.sendEmail();
//            }
//        }).start();
    }

    public void saveNewEmail(){
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter(file.getPath()));
            wr.write(subject_textField.getText());
            wr.append("\n");
            wr.append(content_areaField.getText());
            wr.flush();
            wr.close();
            scanner = new Scanner(new BufferedReader(new FileReader(file)));
        }catch (Exception e){ e.printStackTrace();}

        subject_textField.setText("");
        content_areaField.setText("");
        subject_textField.setText(scanner.nextLine());
        while (scanner.hasNext()) {
            content_areaField.setText(scanner.nextLine());
        }
        scanner = null;
    }

    public void setSavedTemplates(){
        File file = new File("E:\\InteliJ\\ElectraCodeSystem" );
        System.out.println(this.getClass().getResource("/style.css").getPath());
    }

    @Autowired
    IServer server;

    public void loadUsers(){

        ID.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        Username.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        Email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        usersList.clear();
        usersList.addAll(server.getAllUsers());
        usersTableView.setItems(usersList);
    }

    //check orders button action download data from database
    //*
    // *

    public void addRecord(){

//        ol.add(new Orders(3,1, "Razor Electra V2", 2, 440, "Karol", "Nowak", "18-400 Lomza", date));
//        ol.add(new Orders(3,3, "Razor Kraken PRO", 1, 300, "Karol", "Nowak", "18-400 Lomza", date));
//        ol.add(new Orders(3,7, "ThreadRipper", 1, 4000, "Karol", "Nowak", "18-400 Lomza", date));
//        ol.add(new Orders(6,127, "order 66 v2", 4, 14, "Kamil", "Namek", "23-450 Miastkowo", date));
//        ol.add(new Orders(13,257, "GTX 1050Ti", 1, 721, "Kazimierz", "Wiesiek", "45-24 Mielno", date));
//        ol.add(new Orders(13,314, "Intel Core i7", 1, 1223, "Kazimierz", "Wiesiek", "45-24 Mielno", date));
//        ol.add(new Orders(6,178, "order 66", 4, 14, "Kamil", "Namek", "23-450 Miastkowo", date));
//        ol.add(new Orders(13,216, "AMD Ryzen 7", 1, 1223, "Kazimierz", "Wiesiek", "45-24 Mielno", date));
//        ol.add(new Orders(5,43, "CORsair oc", 1, 120, "Adam", "Piaseczny", "67-800 Ciechanow", date));
//        ol.add(new Orders(15,6,"Dell",3,5,"Linkin","Park","90-300 Prochowice",date));

        ol.addAll(server.getAllOrders());

        ordersTableView.setItems(ol);
    }

    //*
    // *
    //check orders button action download data from database END

    //check products button action for tests -- change to data from database
    //*
    // *

    public void addProducts(){

        IDProduct.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        ProductNameAv.setCellValueFactory(new PropertyValueFactory<Products, String>("productName"));
        AmountAv.setCellValueFactory(new PropertyValueFactory<Products, Integer>("amount"));

        productsList.add(new Products(1, "Razor Electra V2", 500, 440));
        productsList.add(new Products(2, "Razor Kraken PRO", 52, 300));
        productsList.add(new Products(3, "ThreadRipper", 500, 4000));
        productsList.add(new Products(4, "GTX 1050Ti", 400, 721));
        productsList.add(new Products(5, "Intel Core i7", 500, 1223));
        productsList.add(new Products(6, "AMD Ryzen 7", 500, 1223));
        productsList.add(new Products(7, "CORsair oc", 500, 120));
        productsList.add(new Products(8, "Dell", 32, 5));
        productsList.add(new Products(9, "Asus Rouge Dominus Extreme", 500, 4700));



        AmountAv.setCellFactory(column -> {
            return new TableCell<Products, Integer>(){
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    TableRow table = getTableRow();

                    if (!isEmpty()) {
                        setText(""+item);
                        if (item < 100)
                       table.setStyle("-fx-background-color: #ed533b");
                    }

                }
            };

        });

        productsTableView.setItems(productsList);
    }

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

    //*
    // *
    //check products button action for tests -- chande to data from database END


    //changed all checkboxes to selected
    //*
    // *

    public void selectAll(){

        for (Orders ord : ol)
            ord.getCheckBox().setSelected(true);

    }

    //*
    // *
    //changed all checkboxes to selected END



    //generate recipies for customers
    //*
    // *

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

            orderDetailListView.getItems().add("Order "+c.getKey()); //set value of listView
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

                        if (j == 10){
                            personalDetailsMapper.get(c.getKey()).add(buff[j]);
                            setDetailsOnce = false;
                        }
                    }

                    //gather only information about customer from first row then active lock END

                    //gather information about products  //

                    if (j % 14 == 1)
                        productsDetails.add(buff[j]);
                    if (j % 14 == 2)
                        productsDetails.add(buff[j]);
                    if (j % 14 == 3)
                        productsDetails.add(buff[j]);
                    if (j % 14 == 4) {
                        productsDetails.add(buff[j]);
                        man = new String[]{productsDetails.toString()}; //convert list to string[]
                       productsDetailsMapper.get(c.getKey()).add(man);  //add string[] to suitable list - Map<Integer, List<String[]>>
                       productsDetails.removeAll(productsDetails); //clear all list
                    }
                    if (j % 11 == 0){
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
    }

    //*
    // *
    //generate recipies for customers



    //set all Customer Recipe site
    // *
    // *
    // *
    // *

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
        l = personalDetailsMapper.get(Integer.parseInt(String.valueOf(((String) orderDetailListView.getSelectionModel().getSelectedItem()).substring(6))));


        //set customer details
        nameSurnameLabel.setText(l.get(0)+" "+l.get(1));
        addressLabel.setText(l.get(2)+" "+l.get(3));
        streetLabel.setText(l.get(4)+" "+l.get(5));

        // get element from map Map<Integer, List<String[]>>, there is list only products : idProduct, productName, amount, price
        ban = productsDetailsMapper.get(Integer.parseInt(String.valueOf(orderDetailListView.getSelectionModel().getSelectedItem()).substring(6)));


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


    // procedure set beginning settings of software

    Gauge gauge;
    Products products;
    User user;
    List<String> dateStringTab;
    List<UserOrderDate> dateAndOrderNumberList;
    Map<Integer, Map<String, Integer>> idUserMonthsAmount;
    Set keys;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    Integer[] digitMonths = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ,11, 12};
    String[] splitedMonths = null;
    String singleDate = "";
    int amountOfItems = 0;
    LineChart<String, Number> userLineChart;
    XYChart.Series series2;
    CategoryAxis xAxis2 = new CategoryAxis();
    NumberAxis yAxis2 = new NumberAxis();
    int maxYChart = 0;
    Map<String, Integer> pastOrders;

    @FXML
    public void initialize() {

        gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SPACE_X).maxValue(500)
                .barColor(Color.rgb(46, 68, 140))
                .threshold(340)
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
                    gauge.setValue(420);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("Jan",23));
        series.getData().add(new XYChart.Data("Feb",3));
        series.getData().add(new XYChart.Data("Mar",14));
        series.getData().add(new XYChart.Data("Apr",30));
        series.getData().add(new XYChart.Data("May",25));
        series.getData().add(new XYChart.Data("Jun",29));
        series.getData().add(new XYChart.Data("Jul",23));
        series.getData().add(new XYChart.Data("Aug",20));
        series.getData().add(new XYChart.Data("Sep",27));
        series.getData().add(new XYChart.Data("Oct",34));
        series.getData().add(new XYChart.Data("Nov",19));
        series.getData().add(new XYChart.Data("Dec",24));
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);

        lineChart.setPrefSize(799, 307);
        dashBoardLineChartPane.getChildren().add(lineChart);
        dashBoardLineChartPane.getChildren().get(dashBoardLineChartPane.getChildren().size()-1).setLayoutX(0);
        dashBoardLineChartPane.getChildren().get(dashBoardLineChartPane.getChildren().size()-1).setLayoutY(86);



        // x, y = 0 , 86

        // w, h = 799, 307

        //serwer.getAllUsers();
        dashBoardInformation.toFront();
        topLabel.setText(dashBoard.getText());
        loadUsers(); //add users to user tab to userTableView
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

        manageProductsTableView.setOnMouseClicked(event -> {

            products = manageProductsTableView.getSelectionModel().getSelectedItem();
            manegeProductIDLabel.setText(""+products.getId());
            manegeProductNameLabel.setText(products.getProductName());
            manegeProductPriceLabel.setText(""+products.getPrice());
        });

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

        });



        productsDetailsMapper = new HashMap<>();
        personalDetailsMapper = new HashMap<>();
        panes.add(vbox2);
        panes.add(vbox3);

        userShoppingPeriodMap = new HashMap<>();
        amountOfBoughtItems = new HashMap<>();

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

    }

    // procedure set beginning settings of software END


    public void initOrderTable()
    {

        OrderNumber.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("orderNumber"));
        ProductID.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("productID"));
        ProductName.setCellValueFactory(new PropertyValueFactory<Orders, String>("productName"));
        Amount.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("amount"));
        Price.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("price"));
        Name.setCellValueFactory(new PropertyValueFactory<Orders, String>("name"));
        Surname.setCellValueFactory(new PropertyValueFactory<Orders, String>("surname"));
        Town.setCellValueFactory(new PropertyValueFactory<Orders, String>("town"));
        TownCode.setCellValueFactory(new PropertyValueFactory<Orders, String>("townCode"));
        Street.setCellValueFactory(new PropertyValueFactory<Orders, String>("street"));
        HouseNumber.setCellValueFactory(new PropertyValueFactory<Orders, String>("houseNumber"));
        //State.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("state"));
        orderDate.setCellValueFactory(new PropertyValueFactory<Orders, Date>("date"));
        Action.setCellValueFactory(new PropertyValueFactory<Orders, String>("checkBox"));

        ordersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ordersTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    System.out.println(event.getClickCount());
                    Orders ord = (Orders) ordersTableView.getSelectionModel().getSelectedItem();
                    System.out.println(ord.getProductName());
                    ordersTableView.getItems().remove(ord);
                }
            }
        });

    }



    // hide roll down sub bar lists
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
    public void changeFocusedButtonBackgroundColor(Object object){

        for(Node node : getAllNodes(menuVBox)){
            if (node == object){ node.getStyleClass().clear(); node.getStyleClass().add("selectedButton"); }
            else{ node.getStyleClass().clear(); node.getStyleClass().add("subMenuHover"); }
        }
    }

    //switching between menu bar cards

    @FXML
    private void handleAction(MouseEvent event){

        changeFocusedButtonBackgroundColor(event.getSource());
        gauge.setValue(0);

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
            setSavedTemplates();
        }

        if (event.getSource() == sentProducts){
            setOrderHeaderLabel(sentProducts.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            sentMerchandiseContentPane.toFront();
        }

        if (event.getSource() == dataSet) {
            setOrderHeaderLabel(dataSet.getText());
            topLabel.textProperty().bind(orderHeaderLabelProperty());
            hideLists();
            click = true;
            dataSetContentPane.toFront();
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
            gauge.setValue(420);
            dashBoardInformation.toFront();
        }
    }

    //switching between menu bar cards  END


    // procedure close all program


    @Autowired
    CloseWindowController closeWindowController;

    public AnchorPane getMainPane(){ return mainPane;}

    public void closeWindow() throws Exception{


        closeWindowController.closeWindow();
    }

    // procedure close all program END


}

package desktopApp.controlers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import desktopApp.MailSender.SendEmail;
import desktopApp.api.IServer;
import desktopApp.config.Config;
import desktopApp.implementation.Orders;
import desktopApp.implementation.Products;
import desktopApp.implementation.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;

@Component
public class Controller{


    //private Session

    @Autowired
    SendEmail sendEmail;

    public void sendMessage(){
       //sendEmail.sendEmail();
    }

    //kolor tla #558ae0

    @FXML
    ProgressIndicator prog;

    @FXML
    AnchorPane mainPane;

    @FXML
    Label topLabel; // tabs tilted label

   @FXML
   Button availability, manageProducts, sentMessage, sentProducts; // sub menu buttons

   @FXML
    JFXButton orders, dataSet, users; // menu buttons

    @FXML
    JFXTextField usersSearchTextField;

    @FXML
    VBox vbox2, vbox3; // hidden menu bar

    @FXML
    GridPane usersContentPane, availabilityContentPane, takeSupplyContentPane, sentMessageContentPane, sentMerchandiseContentPane, dataSetContentPane, gridRecipe, recipeProductsGridPane;

    @FXML
    Pane innerGridPaneTiltedPanel; // Customer Recipe pane, contain info about seller

    @FXML
    private Pane manegeProductNamePane,manegeProductIDPane,manegeProductPricePane; //manage product wraped panes

    @FXML
    private Label manegeProductIDLabel,manegeProductNameLabel,manegeProductPriceLabel; //manage products wraped panes labels

    @FXML
    ScrollPane ordersContentPane; //main container of orders tab

    @FXML
    TableView usersTableView, ordersTableView, productsTableView; // user table view

    @FXML
    TableView<Products> manageProductsTableView;

    @FXML
    TableColumn OrderNumber, Location, ProductID, ProductName, Amount, Price, Name, Surname, State, Action; // table columns of orders table in orders tab

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

    // tabs topLabel set methods //

    StringProperty orderHeaderLabel = new SimpleStringProperty("Orders");

    public StringProperty orderHeaderLabelProperty() { return orderHeaderLabel; }

    public void setOrderHeaderLabel(String orderHeaderLabel) { this.orderHeaderLabel.set(orderHeaderLabel); }

    // tabs topLabel set methods END //

    //variable collect rolling vboxes
    ObservableList<VBox> panes = FXCollections.observableArrayList();

    //variables responsible for get and gather users orders

    Map<Integer, List<String[]>> productsDetailsMapper;

    Map<Integer, List<String>> personalDetailsMapper;

    Map<Integer, List> ordersGatheredList = new HashMap<Integer, List>();

    Set<Map.Entry<Integer, List>> setWholeOrders;

    ObservableList<Orders> ol = FXCollections.observableArrayList();

    //variables responsible for get and gather users orders END

    // #1264d1 users list
    ObservableList<User> usersList = FXCollections.observableArrayList();

    // products list
    ObservableList<Products> productsList = FXCollections.observableArrayList();

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

    //check orders button action for tests -- change to data from database
    //*
    // *

    public void addRecord(){

        ol.add(new Orders(3,1, "Razor Electra V2", 2, 440, "Karol", "Nowak", "18-400 Lomza", "oczekujace"));
        ol.add(new Orders(3,3, "Razor Kraken PRO", 1, 300, "Karol", "Nowak", "18-400 Lomza", "oczekujace"));
        ol.add(new Orders(3,7, "ThreadRipper", 1, 4000, "Karol", "Nowak", "18-400 Lomza", "oczekujace"));
        ol.add(new Orders(6,127, "order 66 v2", 4, 14, "Kamil", "Namek", "23-450 Miastkowo", "oczekujace"));
        ol.add(new Orders(13,257, "GTX 1050Ti", 1, 721, "Kazimierz", "Wiesiek", "45-24 Mielno", "oczekujace"));
        ol.add(new Orders(13,314, "Intel Core i7", 1, 1223, "Kazimierz", "Wiesiek", "45-24 Mielno", "oczekujace"));
        ol.add(new Orders(6,178, "order 66", 4, 14, "Kamil", "Namek", "23-450 Miastkowo", "oczekujace"));
        ol.add(new Orders(13,216, "AMD Ryzen 7", 1, 1223, "Kazimierz", "Wiesiek", "45-24 Mielno", "oczekujace"));
        ol.add(new Orders(5,43, "CORsair oc", 1, 120, "Adam", "Piaseczny", "67-800 Ciechanow", "oczekujace"));
        ol.add(new Orders(15,6,"Dell",3,5,"Linkin","Park","90-300 Prochowice","oczekujace"));

        ordersTableView.setItems(ol);
    }

    //*
    // *
    //check orders button action for tests -- change to data from database END

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
                list.add(recipe.get(i + 1));
                ordersGatheredList.put(recipe.get(i + 1).getOrderNumber(), list);
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

        //divide orders to separate lists END

        //lopps to watch map collections----------------------------------------------------------

//        for (Map.Entry v : personalDetailsMapper.entrySet())
//            System.out.println(v.getKey()+" = "+v.getValue());
//
//        for (Map.Entry v : productsDetailsMapper.entrySet())
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
        addressLabel.setText(l.get(2)+"");
        //streetLabel.setText(buff[4]);

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

            recipeProductsGridPane.getRowConstraints().add(new RowConstraints(50));
            for (int j = 0; j < 4; j++) {
                Label l = new Label(js[j]);
                l.setPrefWidth(recipeProductsGridPane.getColumnConstraints().get(j+1).getPrefWidth() + 20);
                l.setAlignment(Pos.CENTER);
                recipeProductsGridPane.add(l, j+1, i+1);
                if (j == 3)
                   utterCoastInt += Integer.parseInt(l.getText().trim());
            }
            pom = "";
            utterCoast.setText(String.valueOf(utterCoastInt));
        }

        for (Map.Entry entry : setWholeOrders)
            System.out.println(entry);
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
                List<String> l = new ArrayList();

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


    @FXML
    public void initialize() {

        //serwer.getAllUsers();
        usersContentPane.toFront();
        loadUsers(); //add users to user tab to userTableView
        addProducts(); //add products to warehouse tab to availability to productsTableView
        addManageProducts(); ////add products to warehouse tab to manage products to manageproductsTableView
        initOrderTable(); //initialize orderTableView

        Label title = new Label();
        title.setGraphic(new Label("Dealer  "));
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

            Products products = manageProductsTableView.getSelectionModel().getSelectedItem();
            manegeProductIDLabel.setText(""+products.getId());
            manegeProductNameLabel.setText(products.getProductName());
            manegeProductPriceLabel.setText(""+products.getPrice());
        });


        innerGridPaneTiltedPanel.getChildren().add(title);
        innerGridPaneTiltedPanel.setStyle("-fx-border-style: solid inside;");

        productsDetailsMapper = new HashMap<>();
        personalDetailsMapper = new HashMap<>();
        panes.add(vbox2);
        panes.add(vbox3);

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
        Location.setCellValueFactory(new PropertyValueFactory<Orders, String>("location"));
        State.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("state"));
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

    //switching between menu bar cards

    @FXML
    private void handleAction(MouseEvent event){

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
            usersContentPane.toFront();
            hideLists();
            click = true;
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

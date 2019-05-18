package desktopApp.implementation;

import desktopApp.api.IOrders;
import javafx.beans.property.*;
import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Orders implements IOrders {

    private IntegerProperty productID, amount, price, orderNumber;
    private StringProperty town, townCode, street, houseNumber, productName, state, name, surname;
    private BooleanProperty checkProperty;
    private Date date;
    private CheckBox checkBox;

    public Orders(int orderNumber, int productID, String productName, int amount, int price,
                  String name, String surname, String town, String townCode,
                  String street, String houseNumber, Date date /*String state*/){

        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.amount = new SimpleIntegerProperty(amount);
        this.price = new SimpleIntegerProperty(price);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.town = new SimpleStringProperty(town);
        this.townCode = new SimpleStringProperty(townCode);
        this.street = new SimpleStringProperty(street);
        this.houseNumber = new SimpleStringProperty(houseNumber);
        this.date = new Date(); //this.state = new SimpleStringProperty(state);
        this.checkProperty = new SimpleBooleanProperty(false);
        checkBox = new CheckBox();
    }

    public int getOrderNumber(){ return orderNumber.get(); }

    @Override
    public String getTown() { return town.get(); }

    @Override
    public String getTownCode() { return townCode.get(); }

    @Override
    public String getStreet() { return street.get(); }

    @Override
    public String getHouseNumber() { return houseNumber.get(); }

    public int getProductID() { return productID.get(); }

    public String getProductName() { return productName.get(); }

    public int getAmount() { return amount.get(); }

    public int getPrice() { return price.get(); }

    public String getName(){ return name.get(); }

    public String getSurname(){ return surname.get(); }

    //public String getState(){ return state.get(); }

    public LocalDate getDate(){ return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); }

    public CheckBox getCheckBox(){ return checkBox; }

    @Override
    public String toString() {
        return getOrderNumber()+"/"+getProductID()+"/"+getProductName()+"/"+getAmount()+"/"+getPrice()+"/"+getName()+"/"
            +getSurname()+"/"+getTown()+"/"+getTownCode()+"/"+getStreet()+"/"+getHouseNumber()+"/"+getDate();
    }
}


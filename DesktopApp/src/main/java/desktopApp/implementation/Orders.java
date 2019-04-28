package desktopApp.implementation;

import desktopApp.api.IOrders;
import javafx.beans.property.*;
import javafx.scene.control.CheckBox;

public class Orders implements IOrders {

    private IntegerProperty productID, amount, price, orderNumber;
    private StringProperty location, productName, state, name, surname;
    private BooleanProperty checkProperty;
    private CheckBox checkBox;

    public Orders(int orderNumber, int productID, String productName, int amount, int price, String name, String surname, String location, String state){
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.amount = new SimpleIntegerProperty(amount);
        this.price = new SimpleIntegerProperty(price);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.location = new SimpleStringProperty(location);
        this.state = new SimpleStringProperty(state);
        this.checkProperty = new SimpleBooleanProperty(false);
        checkBox = new CheckBox();
    }

    public int getOrderNumber(){ return orderNumber.get(); }

    public String getLocation() { return location.get(); }

    public int getProductID() { return productID.get(); }

    public String getProductName() {
        return productName.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public int getPrice() {
        return price.get();
    }

    public String getName(){ return name.get(); }

    public String getSurname(){ return surname.get(); }

    public String getState(){ return state.get(); }

    public CheckBox getCheckBox(){ return checkBox; }

    @Override
    public String toString() {
        return getOrderNumber()+"/"+getProductID()+"/"+getProductName()+"/"+getAmount()+"/"+getPrice()+"/"+getName()+"/"
                +getSurname()+"/"+getLocation()+"/"+getState();
    }
}


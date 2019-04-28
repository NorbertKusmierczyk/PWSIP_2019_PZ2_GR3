package desktopApp.implementation;

import desktopApp.api.IProducts;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Products implements IProducts {

    private IntegerProperty id, amount, price;
    private StringProperty productName;

    public Products(int id, String productName, int amount, int price) {
        this.id = new SimpleIntegerProperty(id);
        this.productName = new SimpleStringProperty(productName);
        this.amount = new SimpleIntegerProperty(amount);
        this.price = new SimpleIntegerProperty(price);
    }

    @Override
    public int getId() {
        return id.get();
    }

    @Override
    public String getProductName() {
        return productName.get();
    }

    @Override
    public int getAmount() {
        return amount.get();
    }

    @Override
    public int getPrice() {
        return price.get();
    }
}

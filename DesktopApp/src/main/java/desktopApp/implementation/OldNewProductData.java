package desktopApp.implementation;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OldNewProductData {

    //IntegerProperty oldID, newID, oldPrice, newPrice;
    StringProperty oldID, newID, oldPrice, newPrice, oldName, newName;

    public OldNewProductData() {
        this.oldID = new SimpleStringProperty();
        this.newID = new SimpleStringProperty();
        this.oldPrice = new SimpleStringProperty();
        this.newPrice = new SimpleStringProperty();
        this.oldName = new SimpleStringProperty();
        this.newName = new SimpleStringProperty();
    }

    public String getOldID() {
        return oldID.get();
    }

    public StringProperty oldIDProperty() {
        return oldID;
    }

    public void setOldID(String oldID) {
        this.oldID.set(oldID);
    }

    public String getNewID() {
        return newID.get();
    }

    public StringProperty newIDProperty() {
        return newID;
    }

    public void setNewID(String newID) {
        this.newID.set(newID);
    }

    public String getOldPrice() {
        return oldPrice.get();
    }

    public StringProperty oldPriceProperty() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice.set(oldPrice);
    }

    public String getNewPrice() {
        return newPrice.get();
    }

    public StringProperty newPriceProperty() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice.set(newPrice);
    }

    public String getOldName() {
        return oldName.get();
    }

    public StringProperty oldNameProperty() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName.set(oldName);
    }

    public String getNewName() {
        return newName.get();
    }

    public StringProperty newNameProperty() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName.set(newName);
    }
}

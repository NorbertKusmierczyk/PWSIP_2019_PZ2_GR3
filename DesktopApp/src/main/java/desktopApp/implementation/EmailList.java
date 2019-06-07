package desktopApp.implementation;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class EmailList {

    private StringProperty idUserMailUser, supply;
    private ComboBox<String> comboBox;

    public EmailList(String idUserMailUser, String supply) {
        this.idUserMailUser = new SimpleStringProperty(idUserMailUser);
        this.supply = new SimpleStringProperty(supply);
        this.comboBox = new ComboBox();
    }

    public String getIdUserMailUser() { return idUserMailUser.get(); }

    public String getSupply(){ return supply.get(); }

    public ComboBox getComboBox() { return comboBox; }

//    public String toString(){
//        return getIdUserMailUser()+"";
//    }
}

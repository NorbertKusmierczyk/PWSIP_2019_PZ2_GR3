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

    private StringProperty idUserMailUser;
    private ComboBox comboBox;

    ObservableList<String> tw = FXCollections.observableArrayList();

    public EmailList(String idUserMailUser) {
        tw.add("email1");
        this.idUserMailUser = new SimpleStringProperty(idUserMailUser);
        this.comboBox = new ComboBox(tw);
    }

    public String getIdUserMailUser() { return idUserMailUser.get(); }

    public ComboBox getComboBox() { return comboBox; }
}

package desktopApp.implementation;

import desktopApp.api.IUserOrderDate;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Date;

public class UserOrderDate implements IUserOrderDate {

    private SimpleIntegerProperty id, ilosc;
    private Date date;

    public UserOrderDate(int id, Date date, int ilosc) {
        this.id = new SimpleIntegerProperty(id);
        this.date = date;
        this.ilosc = new SimpleIntegerProperty(ilosc);
    }

    @Override
    public int getOrderID() { return id.get();}

    @Override
    public Date getLocalDate() { return date;}

    @Override
    public int getIlosc() { return ilosc.get(); }
}

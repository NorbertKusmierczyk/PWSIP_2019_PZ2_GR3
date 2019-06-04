package desktopApp.implementation;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "userTab")
public class UserOrderHibernate {

//    public String toString(){
//        return id+" "+ilosc;
//    }

    @Id
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "ilosc", unique = false)
    private int ilosc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public int getIlosc() {
        return ilosc;
    }
}

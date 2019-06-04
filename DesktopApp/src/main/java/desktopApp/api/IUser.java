package desktopApp.api;

import java.time.LocalDate;
import java.util.Date;

public interface IUser {

    public int getId();

    public String getUserName();

    public String getEmail();

    public Date getDate();

    public String getPassword();
}

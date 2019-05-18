package desktopApp.implementation;

import desktopApp.api.IUserDAO;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAO implements IUserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<User> setAllUsers() {
        List<User> users = jdbcTemplate.query("select id, log, email from users", new UserMapper());
        return users;
    }

    public List<Orders> getAllOrders(){
        List<Orders> orders = jdbcTemplate.query("select * from `orders` where extract(month from `datazamowienia`) = (select extract(month from CURRENT_DATE()));", new OrdersMapper());
        return orders;
    }

    @Override
    public List<UserOrderDate> getAllOrdersDate() {
        List<UserOrderDate> list = jdbcTemplate.query("select id, dataZamowienia, ilosc from orders", new UserOrderDateMapper());
        return list;
    }

    private static class UserOrderDateMapper implements RowMapper<UserOrderDate>{

        @Override
        public UserOrderDate mapRow(ResultSet resultSet, int i) throws SQLException {
            UserOrderDate userOrderDate = new UserOrderDate(
                    resultSet.getInt("id"),
                    resultSet.getDate("dataZamowienia"),
                    resultSet.getInt("ilosc")
            );
            return userOrderDate;
        }
    }

    private static class OrdersMapper implements RowMapper<Orders>{

        @Override
        public Orders mapRow(ResultSet resultSet, int i) throws SQLException {
            Orders orders = new Orders(
                    resultSet.getInt("id"),
                    resultSet.getInt("idProduktu"),
                    resultSet.getString("name"),
                    resultSet.getInt("ilosc"),
                    resultSet.getInt("cena"),
                    resultSet.getString("imie"),
                    resultSet.getString("nazwisko"),
                    resultSet.getString("miasto"),
                    resultSet.getString("kod"),
                    resultSet.getString("adres"),
                    resultSet.getString("numerDomu"),
                    resultSet.getDate("dataZamowienia")

            );
            return orders;
        }
    }

    private static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User(resultSet.getInt("id"),
                    resultSet.getString("log"),
                    resultSet.getString("email"));
            return user;
        }
    }
}

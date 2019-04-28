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
        List<User> users = jdbcTemplate.query("select * from darkside", new UserMapper());
        return users;
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

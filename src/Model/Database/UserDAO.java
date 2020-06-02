package Model.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import Model.Entity.User;
import java.sql.Connection;

public class UserDAO {

    public ResultSet findAll() throws Exception{
        PreparedStatement statement;
        Connection connection = DBConnector.getInstance().getConnection();

        statement = connection.prepareStatement("SELECT * FROM User;");
        ResultSet rs = statement.executeQuery();

        return rs;
    }
}

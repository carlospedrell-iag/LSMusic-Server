package Model.Database;

import java.sql.*;
import java.util.ArrayList;
import Model.Entity.User;

import java.time.LocalDateTime;

public class UserDAO {
    private Connection connection;

    public UserDAO(){}{
        this.connection = DBConnector.getInstance().getConnection();
    }

    public ArrayList<User> findAll(){
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("SELECT * FROM User;");
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Timestamp last_access_ts = rs.getTimestamp("last_access");
                LocalDateTime last_access = null;
                if(last_access_ts != null){
                    last_access = last_access_ts.toLocalDateTime();
                }

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        last_access
                );
                users.add(user);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    public User findByName(String name){
        PreparedStatement statement;
        User user;

        try{
            statement = connection.prepareStatement("SELECT * FROM User WHERE name = ?;");
            statement.setString(1,name);
            ResultSet rs = statement.executeQuery();
            rs.next();

            Timestamp last_access_ts = rs.getTimestamp("last_access");
            LocalDateTime last_access = null;
            if(last_access_ts != null){
                last_access = last_access_ts.toLocalDateTime();
            }

            user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    last_access
            );

            return user;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public void create(User user){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("INSERT INTO User VALUES(default,?,?,?,?,?)");

            Timestamp created_at = Timestamp.valueOf(user.getCreated_at());

            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setTimestamp(4,created_at);
            statement.setTimestamp(5,created_at);

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Usuari " + user.getName() + " afegit a la db.");
    }

    public void update(User user){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("UPDATE User SET " +
                    "name = ?," +
                    "email = ?," +
                    "password = ?," +
                    "created_at = ?," +
                    "last_access = ? " +
                    "WHERE id = ?;");

            Timestamp created_at = Timestamp.valueOf(user.getCreated_at());
            Timestamp last_access = Timestamp.valueOf(user.getLast_access());

            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setTimestamp(4,created_at);
            statement.setTimestamp(5,last_access);
            statement.setInt(6,user.getId());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Usuari " + user.getName() + " actualitzat a la db.");
    }

    public void deleteByName(String name){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("DELETE FROM User WHERE name = ?");

            statement.setString(1,name);

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Usuari " + name + " eliminat de la db.");
    }
}

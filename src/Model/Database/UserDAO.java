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

                user.setPlaylist_count(findPlaylistCount(user));
                user.setTrack_count(findTrackCount(user));
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


            if(rs.next() == true){
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
            }


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

    public ArrayList<User> findFollowing(User user){
        PreparedStatement statement;
        ArrayList<User> users = new ArrayList<>();
        System.out.println("Following Request");

        try{
            statement = connection.prepareStatement("SELECT * FROM Follows AS F JOIN User U ON F.id_followed = U.id WHERE F.id_follower = ?;");
            statement.setInt(1,user.getId());
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Timestamp last_access_ts = rs.getTimestamp("last_access");
                LocalDateTime last_access = null;
                if(last_access_ts != null){
                    last_access = last_access_ts.toLocalDateTime();
                }
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        last_access
                );
                users.add(u);
            }
            return users;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public void followUser(User user, User followed){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("INSERT INTO Follows VALUES(?,?)");
            statement.setInt(1,user.getId());
            statement.setInt(2,followed.getId());
            statement.executeUpdate();
            System.out.println("User " + user.getName() + " is now following " + followed.getName());

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private int findPlaylistCount(User user){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("SELECT COUNT(id) FROM Playlist WHERE id_user = ?;");
            statement.setInt(1,user.getId());
            ResultSet rs = statement.executeQuery();
            rs.next();

            return rs.getInt("COUNT(id)");

        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    private int findTrackCount(User user){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("SELECT COUNT(PT.id_playlist_track) FROM Playlist_Track AS PT " +
                    "JOIN Playlist P ON PT.id_playlist = P.id JOIN User U ON P.id_user = U.id WHERE U.id = ?;");
            statement.setInt(1,user.getId());
            ResultSet rs = statement.executeQuery();
            rs.next();

            return rs.getInt("COUNT(PT.id_playlist_track)");

        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }
}

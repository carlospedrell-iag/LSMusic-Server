package Model.Database;

import java.sql.*;
import java.util.ArrayList;
import Model.Entity.Track;

public class TrackDAO {
    private Connection connection;

    public TrackDAO(){}{
        this.connection = DBConnector.getInstance().getConnection();
    }

    public ArrayList<Track> findAll(){
        ArrayList<Track> tracks = new ArrayList<>();
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("SELECT * FROM Track;");
            ResultSet rs = statement.executeQuery();

            while(rs.next()){

                Track track = new Track(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"),
                        rs.getString("path"),
                        rs.getInt("plays"),
                        rs.getFloat("rating")
                );
                tracks.add(track);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return tracks;
    }

    public Track findById(int id){
        PreparedStatement statement;
        Track track;

        try{
            statement = connection.prepareStatement("SELECT * FROM Track WHERE id = ?;");
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){

                track = new Track(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"),
                        rs.getString("path"),
                        rs.getInt("plays"),
                        rs.getFloat("rating")
                );

                return track;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void create(Track track){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("INSERT INTO Track VALUES(default,?,?,?,?,?,default,default)");

            statement.setString(1,track.getTitle());
            statement.setString(2,track.getArtist());
            statement.setString(3,track.getAlbum());
            statement.setString(4,track.getGenre());
            statement.setString(5,track.getPath());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Track " + track.getTitle() + " afegit a la db.");
    }

    public void update(Track track){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("UPDATE Track SET " +
                    "title = ?," +
                    "artist = ?," +
                    "album = ?," +
                    "genre = ?," +
                    "path = ?," +
                    "plays = ?," +
                    "rating = ?" +
                    "WHERE id = ?;");

            statement.setString(1,track.getTitle());
            statement.setString(2,track.getArtist());
            statement.setString(3,track.getAlbum());
            statement.setString(4,track.getGenre());
            statement.setString(5,track.getPath());
            statement.setInt(6,track.getPlays());
            statement.setFloat(7,track.getRating());
            statement.setInt(8,track.getId());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteById(int id){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("DELETE FROM Track WHERE id = ?");
            statement.setInt(1,id);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Track id:" + id + " eliminat de la db.");
    }
}

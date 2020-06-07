package Model.Database;

import Model.Entity.PlaylistTrack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistTrackDAO {

    private Connection connection;

    public PlaylistTrackDAO(){
        this.connection = DBConnector.getInstance().getConnection();
    }

    public void create(PlaylistTrack playlistTrack){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("INSERT INTO Playlist_Track VALUES(default,?,?,default)");

            statement.setInt(1,playlistTrack.getPlaylist_id());
            statement.setInt(2,playlistTrack.getTrack_id());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Playlist Track id:" + playlistTrack.getTrack_id() + " afegit a la db.");
    }

}

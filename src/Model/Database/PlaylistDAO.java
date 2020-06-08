package Model.Database;

import Model.Entity.Playlist;
import Model.Entity.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistDAO {
    private Connection connection;

    public PlaylistDAO(){
        this.connection = DBConnector.getInstance().getConnection();
    }

    public ArrayList<Playlist> findAllByUserId(int user_id){
        ArrayList<Playlist> playlists = new ArrayList<>();
        PreparedStatement statement;

        try{
            System.out.println("Playlists Request");
            statement = connection.prepareStatement("SELECT * FROM Playlist WHERE id_user = ?");
            statement.setInt(1,user_id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int playlist_id = rs.getInt("id");

                ArrayList<Track> playlist_tracks = findPlaylistTracksById(playlist_id);

                Playlist playlist = new Playlist(
                        playlist_id,
                        rs.getString("name"),
                        rs.getInt("id_user"),
                        playlist_tracks
                );

                playlists.add(playlist);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return playlists;
    }

    public ArrayList<Track> findPlaylistTracksById(int playlist_id){
        ArrayList<Track> tracks = new ArrayList<>();
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("SELECT * FROM Playlist_Track JOIN Track T ON Playlist_Track.id_track = T.id WHERE id_playlist = ?");
            statement.setInt(1,playlist_id);
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

                track.setPlaylist_track_id(rs.getInt("id_playlist_track"));

                tracks.add(track);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return tracks;
    }

    public void create(Playlist playlist){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("INSERT INTO Playlist VALUES(default,?,?)");
            statement.setString(1,playlist.getName());
            statement.setInt(2,playlist.getId_user());

            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Playlist " + playlist.getName() + " afegida a la db.");
    }

    public void deleteById(int id){
        PreparedStatement statement;

        try{
            statement = connection.prepareStatement("DELETE FROM Playlist WHERE id = ?");
            statement.setInt(1,id);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("Playlist id: " + id + " eliminada de la db.");
    }
}

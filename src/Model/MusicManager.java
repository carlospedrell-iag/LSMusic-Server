package Model;

import Model.Database.PlaylistDAO;
import Model.Database.PlaylistTrackDAO;
import Model.Database.TrackDAO;
import Model.Entity.ObjectMessage;
import Model.Entity.Playlist;
import Model.Entity.PlaylistTrack;
import Model.Entity.User;

import java.util.ArrayList;

public class MusicManager {

    public static ObjectMessage requestTracklist(ObjectMessage om){
        TrackDAO trackDAO = new TrackDAO();
        om.setObject(trackDAO.findAll());
        return om;
    }

    public static ObjectMessage requestPlaylists(ObjectMessage om){
        User user = (User)om.getObject();
        PlaylistDAO playlistDAO = new PlaylistDAO();
        ArrayList<Playlist> playlists = playlistDAO.findAllByUserId(user.getId());
        om.setObject(playlists);
        return om;
    }

    public static ObjectMessage newPlaylist(ObjectMessage om){
        PlaylistDAO playlistDAO = new PlaylistDAO();
        playlistDAO.create((Playlist)om.getObject());

        return om;
    }

    public static ObjectMessage addPlaylistTrack(ObjectMessage om){
        PlaylistTrackDAO playlistTrackDAO = new PlaylistTrackDAO();
        playlistTrackDAO.create((PlaylistTrack)om.getObject());

        return om;
    }
}

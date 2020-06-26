package Model;

import Model.Database.PlaylistDAO;
import Model.Database.PlaylistTrackDAO;
import Model.Database.TrackDAO;
import Model.Entity.*;

import java.io.*;
import java.net.Socket;
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

    public static ObjectMessage deletePlaylist(ObjectMessage om){
        PlaylistDAO playlistDAO = new PlaylistDAO();
        PlaylistTrackDAO playlistTrackDAO = new PlaylistTrackDAO();
        Playlist playlist = (Playlist)om.getObject();
        playlistDAO.deleteById(playlist.getId());

        for(Track t: playlist.getTracks()){
            playlistTrackDAO.updateRating(t.getId());
        }

        return om;
    }

    public static ObjectMessage addPlaylistTrack(ObjectMessage om){
        PlaylistTrackDAO playlistTrackDAO = new PlaylistTrackDAO();
        playlistTrackDAO.create((PlaylistTrack)om.getObject());

        return om;
    }

    public static ObjectMessage deletePlaylistTrack(ObjectMessage om){
        PlaylistTrackDAO playlistTrackDAO = new PlaylistTrackDAO();
        PlaylistTrack playlistTrack = (PlaylistTrack)om.getObject();
        playlistTrackDAO.deleteById(playlistTrack.getId());
        playlistTrackDAO.updateRating(playlistTrack.getTrack_id());

        return om;
    }

    public static ObjectMessage rateTrack(ObjectMessage om){
        PlaylistTrackDAO playlistTrackDAO = new PlaylistTrackDAO();
        PlaylistTrack playlistTrack = (PlaylistTrack)om.getObject();

        playlistTrackDAO.update(playlistTrack);
        playlistTrackDAO.updateRating(playlistTrack.getTrack_id());

        return om;
    }


    public static ObjectMessage updatePlaycount(ObjectMessage om){
        Track user_track = (Track) om.getObject();

        //afegim la reproducció al playcount
        TrackDAO trackDAO = new TrackDAO();
        Track db_track = trackDAO.findById(user_track.getId());

        db_track.addPlay();
        trackDAO.update(db_track);

        return om;
    }
}

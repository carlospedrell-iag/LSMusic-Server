package Model;

import Model.Database.PlaylistDAO;
import Model.Database.PlaylistTrackDAO;
import Model.Database.TrackDAO;
import Model.Entity.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    //TODO: abrir cancion desde musicpanel no sale downloading...
    public static ObjectMessage getFile(ObjectMessage om){
        Track track = (Track) om.getObject();
        File file = new File(track.getPath());
        System.out.println("Track request, id: " +  track.getId() + " , title: " + track.getTitle());

        try{
            FileInputStream fs = new FileInputStream(track.getPath());
            byte content[] = new byte[(int)file.length()];
            fs.read(content,0,content.length);
            track.setFile(content);
            om.setObject(track);

        } catch (IOException e){
            e.printStackTrace();
        }

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

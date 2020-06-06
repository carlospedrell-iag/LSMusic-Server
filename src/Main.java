import Controller.MusicController;
import Controller.UserController;
import Model.Database.PlaylistDAO;
import Model.DedicatedServer;
import Model.Entity.Playlist;
import Model.Entity.Track;
import View.MainWindow;


import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){


        DedicatedServer server = new DedicatedServer();

        try{
            server.startServer();
        } catch (IOException e){
            e.printStackTrace();
        }

        MainWindow mainWindow = new MainWindow();

        UserController userController = new UserController(mainWindow);
        MusicController musicController = new MusicController(mainWindow);

        mainWindow.getUserPanel().setUpController(userController);
        mainWindow.getMusicPanel().setUpController(musicController);

        /*
        PlaylistDAO playlistDAO = new PlaylistDAO();
        ArrayList<Playlist> playlists = playlistDAO.findAllByUserId(64);

        for(Playlist p:playlists){
            System.out.println("Playlist name: " + p.getName());
            for(Track t:p.getTracks()){
                System.out.println("    -" + t.getTitle());
            }
        }*/
    }
}

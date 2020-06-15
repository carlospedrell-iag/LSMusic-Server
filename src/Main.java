import Controller.MusicController;
import Controller.UserController;
import Model.DedicatedServer;
import View.MainWindow;
import com.formdev.flatlaf.FlatLightLaf;


import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args){

        //instala el flat theme per Swing
        FlatLightLaf.install();
        try{
            UIManager.setLookAndFeel(new FlatLightLaf());
        }catch (Exception e){
            e.printStackTrace();
        }

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
    }
}

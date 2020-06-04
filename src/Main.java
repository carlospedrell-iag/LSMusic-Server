import Controller.UserController;
import Model.DedicatedServer;
import View.MainWindow;


import java.io.IOException;

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
        mainWindow.getUserPanel().setUpController(userController);

    }
}

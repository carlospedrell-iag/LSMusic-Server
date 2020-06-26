package Controller;

import View.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController implements ActionListener {

    MainWindow mainWindow;
    UserController userController;

    public MainController(MainWindow mainWindow, UserController userController){
        this.mainWindow = mainWindow;
        this.userController = userController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "refresh":
                mainWindow.getStatsPanel().refreshTopTen();
                userController.updateTable();
                mainWindow.revalidate();
                mainWindow.getStatsPanel().repaint();
                break;
        }
    }
}

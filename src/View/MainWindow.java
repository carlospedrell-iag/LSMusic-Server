package View;

import javax.swing.*;
import java.io.PrintStream;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel main_panel;
    private UserPanel userPanel;
    private MusicPanel musicPanel;
    private AddTrackDialog addTrackDialog;

    public MainWindow(){
        //settings principals
        setSize(600,540);
        setTitle("LaSalleMusic Server");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.userPanel = new UserPanel();
        this.musicPanel = new MusicPanel();
        //initzialitzem tots els panels
        tabbedPane.add("Manage Users",userPanel.getMain_panel());
        tabbedPane.add("Manage Music",musicPanel.getMain_panel());
        setContentPane(main_panel);
    }

    public void showError(String message){
        JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null,message);
    }

    public int showConfirmMessage(String message){
        return JOptionPane.showConfirmDialog(null,message,"Warning",JOptionPane.YES_NO_OPTION);
    }

    public void showAddTrackDialog(){
        this.addTrackDialog = new AddTrackDialog();
    }


    public AddTrackDialog getAddTrackDialog() {
        return addTrackDialog;
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }

    public MusicPanel getMusicPanel() {
        return musicPanel;
    }
}

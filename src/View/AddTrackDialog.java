package View;

import Controller.MusicController;

import javax.swing.*;

public class AddTrackDialog extends JDialog {
    private JPanel contentPane;
    private JButton button_add;
    private JTextField form_title;
    private JTextField form_artist;
    private JTextField form_album;
    private JTextField form_genre;
    private JButton openFileButton;
    private JLabel file_label;

    public AddTrackDialog() {
        setContentPane(contentPane);
        setModal(false);
        setTitle("Add New Track");
        getRootPane().setDefaultButton(button_add);
        setLocationRelativeTo(null);
        setSize(320,250);
        setResizable(false);
        setVisible(true);


        button_add.setActionCommand("add_track");
        openFileButton.setActionCommand("open_file");
    }

    public String[] getForm(){
        String[] form = {
                form_title.getText(),
                form_artist.getText(),
                form_album.getText(),
                form_genre.getText()
                };
        return form;
    }

    public void setUpController(MusicController controller){
        button_add.addActionListener(controller);
        openFileButton.addActionListener(controller);
    }

    public void setFile_label(String file_label) {
        this.file_label.setText(file_label);
    }
}

package Controller;

import Model.Database.TrackDAO;
import Model.Database.UserDAO;
import Model.Entity.Track;
import View.AddTrackDialog;
import View.MainWindow;
import View.MusicPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MusicController implements ActionListener {

    private MainWindow mainWindow;
    private MusicPanel musicPanel;

    private String file_path = "";
    private final String[] SUPPORTED_FORMATS = {"wav","aiff","au"};

    public MusicController(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        this.musicPanel = mainWindow.getMusicPanel();

        updateTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "show_form":
                file_path = "";
                mainWindow.showAddTrackDialog();
                mainWindow.getAddTrackDialog().setUpController(this);
                break;

            case "add_track":
                addSelectedTrack();
                break;

            case "delete_track":
                removeSelectedTrack();
                break;

            case "open_file":
                showFileChooser();

        }
    }

    private void addSelectedTrack(){
        AddTrackDialog addTrackDialog = mainWindow.getAddTrackDialog();
        Boolean anyEmpty = false;
        String[] form = addTrackDialog.getForm();

        for (String s: form){
            if (s.isBlank()){ anyEmpty = true;}
        }

        if(anyEmpty || file_path.isBlank()){
            mainWindow.showError("S'han d'omplir tots els camps i seleccionar un fitxer");
        } else {
            //si el formulari esta ple, creem una instancia de Track i la enviem al DAO per afegir a la db
            Track track = new Track(form[0],form[1],form[2],form[3],file_path);
            TrackDAO trackDAO = new TrackDAO();
            trackDAO.create(track);
            mainWindow.showMessage("Track afegit a la base de dades");
            mainWindow.getAddTrackDialog().dispose();
            file_path = "";
            updateTable();
        }
    }

    private void removeSelectedTrack(){
        //elimina la canço de la DB de la fila seleccionada per l'usuari

        int selected_row = musicPanel.getMusic_table().getSelectedRow();
        //nomes si l'usuari ha seleccionat
        if(selected_row >= 0){
            //extreiem el titol de la canço de la taula segons quina fila s'ha seleccionat
            String track_title = musicPanel.getMusic_table().getValueAt(selected_row,0).toString();
            //envia un missatge de warning per GUI
            int dialogResult = mainWindow.showConfirmMessage("Eliminar track " + track_title + "?");

            if(dialogResult == JOptionPane.YES_OPTION){
                //eliminem track de la db per nom i actualitzem la taula
                TrackDAO trackDAO = new TrackDAO();
                trackDAO.deleteByTitle(track_title);
                updateTable();

                mainWindow.showMessage("Track " + track_title + " eliminat de la base de dades");
            }
        } else {
            mainWindow.showError("No s'ha seleccionat cap track.");
        }
    }

    private void showFileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Sound Files (wav, aiff, au)",SUPPORTED_FORMATS);
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(mainWindow.getAddTrackDialog());

        if(returnVal == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            this.file_path = file.getAbsolutePath();
            mainWindow.getAddTrackDialog().setFile_label(file.getName());
        }
    }

    private void updateTable(){
        try{
            //recull info d'user de la DB i la envia a la vista per refrescar la taula
            TrackDAO trackDAO = new TrackDAO();

            musicPanel.refreshTable(trackDAO.findAll());
            mainWindow.revalidate();
            System.out.println("Taula musica actualitzada");
        } catch (Exception e){
            mainWindow.showError("Error al connectar a la base de dades");
            mainWindow.revalidate();
        }
    }
}

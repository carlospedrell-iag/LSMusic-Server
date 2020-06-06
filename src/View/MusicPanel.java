package View;

import Controller.MusicController;
import Model.Entity.Track;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class MusicPanel {
    private JPanel main_panel;
    private JTable music_table;
    private JButton addTrack_button;
    private JPanel container;
    private JMenuItem deleteTrack;

    private String[] columnNames = {"Title", "Artist", "Album", "Genre", "File Path"};

    public MusicPanel(){
        music_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        music_table.getTableHeader().setReorderingAllowed(false);
        addTrack_button.setActionCommand("show_form");

        //pop-up menu per eliminar tracks
        JPopupMenu popupMenu = new JPopupMenu();
        deleteTrack = new JMenuItem("Eliminar Track");
        deleteTrack.setActionCommand("delete_track");
        popupMenu.add(deleteTrack);
        music_table.setComponentPopupMenu(popupMenu);
    }

    public void refreshTable(ArrayList<Track> tracks){
        //fem un petit i rapid override de la funcio isCellEditable de DefaultTableModel per desactivar l'edicio de camps
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(columnNames);

        for(Track track: tracks){
            model.addRow(new Object[]{track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getPath()});
        }

        music_table.setModel(model);
    }

    public void setUpController(MusicController controller){
        addTrack_button.addActionListener(controller);
        deleteTrack.addActionListener(controller);
    }

    public JPanel getMain_panel() {
        return main_panel;
    }

    public JTable getMusic_table() {
        return music_table;
    }
}

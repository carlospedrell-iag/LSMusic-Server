package View;

import Controller.UserController;
import Model.Entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserPanel{
    private JPanel main_panel;
    private JButton refresh_button;
    private JTable user_table;

    private JMenuItem deleteUser;

    private String[] columnNames = {"Username", "Created At", "Last Access"};

    public  UserPanel(){
        user_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        user_table.getTableHeader().setReorderingAllowed(false);
        refresh_button.setActionCommand("refresh");

        //pop-up menu quan fem right click per eliminar un user
        JPopupMenu popupMenu = new JPopupMenu();
        deleteUser = new JMenuItem("Eliminar Usuari");
        deleteUser.setActionCommand("delete_user");
        popupMenu.add(deleteUser);
        user_table.setComponentPopupMenu(popupMenu);

    }

    public void refreshTable(ArrayList<User> users){
        //fem un petit i rapid override de la funcio isCellEditable de DefaultTableModel per desactivar l'edicio de camps
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(columnNames);

        for(User user: users){
            //formatejem les dates
            String created_at = user.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(user.getLast_access() != null){
                String last_access = user.getLast_access().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.addRow(new Object[]{user.getName(),created_at,last_access});
            } else {
                model.addRow(new Object[]{user.getName(),created_at,"NULL"});
            }
        }

        user_table.setModel(model);
    }

    public void setUpController(UserController controller){
        refresh_button.addActionListener(controller);
        deleteUser.addActionListener(controller);
    }

    public JPanel getMain_panel() {
        return main_panel;
    }

    public JTable getUser_table() {
        return user_table;
    }
}

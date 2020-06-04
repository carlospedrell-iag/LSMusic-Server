package Controller;

import Model.Database.UserDAO;
import View.MainWindow;
import View.UserPanel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController implements ActionListener {

    private MainWindow mainWindow;
    private UserPanel userPanel;

    public UserController(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        this.userPanel = mainWindow.getUserPanel();

        updateTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "refresh":
                updateTable();
                break;

            case "delete_user":
                removeSelectedUser();
                break;
        }
    }

    private void updateTable(){
        //recull info d'user de la DB i la envia a la vista per refrescar la taula
        System.out.println("Taula users actualitzada");
        UserDAO userDAO = new UserDAO();
        userPanel.refreshTable(userDAO.findAll());
        mainWindow.revalidate();
    }

    private void removeSelectedUser(){
        //elimina l'usuari de la DB de la fila seleccionada per l'usuari

        int selected_row = userPanel.getUser_table().getSelectedRow();
        //nomes si l'usuari ha seleccionat
        if(selected_row >= 0){
            //extreiem el nom de l'usuari de la taula segons quina fila s'ha seleccionat
            String user_name = userPanel.getUser_table().getValueAt(selected_row,0).toString();
            //envia un missatge de warning per GUI
            int dialogResult = mainWindow.showConfirmMessage("Eliminar usuari " + user_name + "?");

            if(dialogResult == JOptionPane.YES_OPTION){
                //eliminem user de la db per nom i actualitzem la taula
                UserDAO userDAO = new UserDAO();
                userDAO.deleteByName(user_name);
                updateTable();

                mainWindow.showMessage("Usuari " + user_name + " eliminat de la base de dades");
            }

        } else {
            mainWindow.showError("No s'ha seleccionat cap usuari.");
        }

    }


}

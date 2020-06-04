package Model;

import Model.Database.UserDAO;
import Model.Entity.User;

import java.util.ArrayList;

public class UserManager {

    public static ArrayList<String> registerUser(User user){
        UserDAO userDAO = new UserDAO();
        ArrayList<String> errors = new ArrayList<>();

        ArrayList<User> users = userDAO.findAll();

        if(user.getEmail().isBlank() || user.getName().isBlank() || user.getPassword().isBlank() || user.getForm_password().isBlank()){
            errors.add("No es poden deixar camps en blanc.");
        } else {

            if(!isEmailValid(user.getEmail())){
                errors.add("Email invalid.");
            }

            if(!isPasswordValid(user.getPassword())){
                errors.add("Contrasenya invalida.");
            }

            if(!user.getPassword().equals(user.getForm_password())){
                errors.add("Les contrasenyes no son iguals.");
            }

            for(User u: users){
                //en cas d'existir el nom d'usuari
                if(u.getName().equals(user.getName())){
                    errors.add("Aquest nom d'usuari ja esta agafat.");
                }

                //en cas d'existir el email
                if(u.getEmail().equals(user.getEmail())){
                    errors.add("Aquest email ja esta agafat.");
                }
            }
        }

        //si no hi ha hagut cap error, s'emmagatzema l'usuari a la db
        if(errors.size() == 0){
            userDAO.create(user);
        }

        return errors;
    }

    private static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private static boolean isPasswordValid(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        return password.matches(regex);
    }
}

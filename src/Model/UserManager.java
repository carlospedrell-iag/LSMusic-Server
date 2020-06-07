package Model;

import Model.Database.UserDAO;
import Model.Entity.ObjectMessage;
import Model.Entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserManager {

    public static ObjectMessage registerUser(ObjectMessage om){
        System.out.println("Objecte rebut.");
        UserDAO userDAO = new UserDAO();
        User user = (User)om.getObject();

        ArrayList<User> users = userDAO.findAll();

        if(user.getEmail().isBlank() || user.getName().isBlank() || user.getPassword().isBlank() || user.getForm_password().isBlank()){
            om.addError("No es poden deixar camps en blanc.");
        } else {

            if(!isEmailValid(user.getEmail())){
                om.addError("Email invalid.");
            }

            if(!isPasswordValid(user.getPassword())){
                om.addError("Contrasenya invalida.");
            }

            if(!user.getPassword().equals(user.getForm_password())){
                om.addError("Les contrasenyes no coincideixen.");
            }

            for(User u: users){
                //en cas d'existir el nom d'usuari
                if(u.getName().equals(user.getName())){
                    om.addError("Aquest nom d'usuari ja esta agafat.");
                }

                //en cas d'existir el email
                if(u.getEmail().equals(user.getEmail())){
                    om.addError("Aquest email ja esta agafat.");
                }
            }
        }

        //si no hi ha hagut cap error, s'emmagatzema l'usuari a la db
        if(om.getErrors().size() == 0){
            userDAO.create(user);
            //demanem l'usuari complet de la db amb la seva id per tornar al client
            om.setObject(userDAO.findByName(user.getName()));
        }

        om.printErrors();

        return om;
    }

    public static ObjectMessage loginUser(ObjectMessage om){
        System.out.println("Objecte rebut.");

        UserDAO userDAO = new UserDAO();
        User user = (User)om.getObject();
        Boolean authenticated = false;
        ArrayList<User> users = userDAO.findAll();

        if(user.getName().isBlank() || user.getPassword().isBlank()){
            om.addError("No es poden deixar camps en blanc.");
        } else {
            for(User u: users){
                //si troba l'usuari per nom o per email(+ password igual)
                if(authenticateByName(u,user) || authenticateByEmail(u,user)){
                    authenticated = true;
                    //logejem l'user actualitzant el last access
                    u.setLast_access(LocalDateTime.now());
                    userDAO.update(u);
                    //posem dins del object message l'usuari validat de la db que conté tota la info
                    om.setObject(u);
                }
            }

            if(!authenticated){
                om.addError("Les credencials introduïdes són incorrectes.");
            }
        }

        om.printErrors();

        return om;
    }

    private static boolean isEmailValid(String email) {
        //expressió regular que ens diu si el mail es valid
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private static boolean isPasswordValid(String password){
        //expressió regular que ens diu si el pass te numeros, minuscules, majuscules i es de 6 o més caracters
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        return password.matches(regex);
    }

    private static boolean authenticateByName(User u, User user){
        return u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword());
    }

    private static boolean authenticateByEmail(User u, User user){
        return u.getEmail().equals(user.getName()) && u.getPassword().equals(user.getPassword());
    }
}

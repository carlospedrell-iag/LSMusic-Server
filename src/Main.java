import Model.Database.UserDAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception {
        UserDAO dao = new UserDAO();


        ResultSet test = dao.findAll();

        ResultSetMetaData rsmd = test.getMetaData();

        System.out.println("Hay " +rsmd.getColumnCount() + " columnas en la db." );


    }
}

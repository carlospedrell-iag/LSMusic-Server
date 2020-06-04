import Model.Database.UserDAO;
import Model.DedicatedServer;
import Model.Entity.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){

        DedicatedServer server = new DedicatedServer();
        try{
            server.startServer();
        } catch (IOException e){
            e.printStackTrace();
        }


    }
}

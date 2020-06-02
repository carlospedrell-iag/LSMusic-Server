package Model.Database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Connection;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private String ip;
    private String db_name;
    private String username;
    private String password;
    private int port;
    private String url;
    private Connection connection;

    private static DBConnector instance;

    public DBConnector(){

        final String config_path = "./config.json";

        try {
            BufferedReader br = new BufferedReader(new FileReader(config_path));
            JsonElement jelement = new JsonParser().parse(br);
            JsonObject jobject = jelement.getAsJsonObject();

            this.ip = jobject.get("ip").getAsString();
            this.db_name = jobject.get("db_name").getAsString();
            this.username = jobject.get("username").getAsString();
            this.password = jobject.get("password").getAsString();
            this.port = jobject.get("port").getAsInt();

        } catch (IOException e){
            e.printStackTrace();
        }

        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + db_name + "?allowPublicKeyRetrieval=true&useSSL=false";

        try {
            this.connection = (Connection) DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connexió exitosa a base de dades "+ db_name );
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al connectar a la base de dades "+url);
        }

    }

    public static DBConnector getInstance(){
        if(instance == null){
            instance = new DBConnector();
        }
        return  instance;
    }

    public Connection getConnection(){
        return this.connection;
    }
}

package Model;

import Model.Database.DBConnector;
import Model.Database.PlaylistDAO;
import Model.Database.TrackDAO;
import Model.Database.UserDAO;
import Model.Entity.ObjectMessage;
import Model.Entity.Playlist;
import Model.Entity.Track;
import Model.Entity.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DedicatedServer extends Thread {

    private ServerSocket server_socket;
    private int client_port;
    private boolean running;

    public DedicatedServer(){
        this.running = false;
        setConfig();

    }

    public void startServer() throws IOException {
        server_socket = new ServerSocket(client_port);
        running = true;
        System.out.println("Servidor initzialitzat.");
        this.start();
    }

    @Override
    public void run(){
        while (running){
            try{
                //conecta al socket del client
                Socket client_socket = server_socket.accept();
                //rebem l'objecte del client
                ObjectInputStream ois = new ObjectInputStream(client_socket.getInputStream());
                Object input_obj = ois.readObject();

                ObjectMessage input_om = (ObjectMessage)input_obj;

                ObjectOutputStream oos = new ObjectOutputStream(client_socket.getOutputStream());
                //segons el tipus d'operacio que ens diu el Object Message, s'executa un metode diferent
                switch (input_om.getMessage()){
                    case "register":
                        input_om = UserManager.registerUser(input_om);
                        oos.writeObject(input_om);
                        break;
                    case "login":
                        input_om = UserManager.loginUser(input_om);
                        oos.writeObject(input_om);
                        break;
                    case "request_tracklist":
                        TrackDAO trackDAO = new TrackDAO();
                        input_om.setObject(trackDAO.findAll());
                        oos.writeObject(input_om);
                        break;
                    case "request_playlists":
                        User user = (User)input_om.getObject();
                        PlaylistDAO playlistDAO = new PlaylistDAO();

                        ArrayList<Playlist> playlists = playlistDAO.findAllByUserId(user.getId());

                        input_om.setObject(playlists);
                        oos.writeObject(input_om);
                }
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }



    private void setConfig(){

        final String config_path = "./config.json";

        try {
            BufferedReader br = new BufferedReader(new FileReader(config_path));
            JsonElement jelement = new JsonParser().parse(br);
            JsonObject jobject = jelement.getAsJsonObject();

            this.client_port = jobject.get("client_port").getAsInt();

        } catch (IOException e){
            e.printStackTrace();
        }
    }


}

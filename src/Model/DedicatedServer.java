package Model;

import Model.Entity.ObjectMessage;
import Model.Entity.Track;
import Model.Entity.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
            processClient();
        }
    }

    private void processClient(){

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
                    input_om = MusicManager.requestTracklist(input_om);
                    oos.writeObject(input_om);
                    break;
                case "request_playlists":
                    input_om = MusicManager.requestPlaylists(input_om);
                    oos.writeObject(input_om);
                    break;
                case "new_playlist":
                    input_om = MusicManager.newPlaylist(input_om);
                    oos.writeObject(input_om);
                    break;
                case "delete_playlist":
                    input_om = MusicManager.deletePlaylist(input_om);
                    oos.writeObject(input_om);
                    break;
                case "add_playlist_track":
                    input_om = MusicManager.addPlaylistTrack(input_om);
                    oos.writeObject(input_om);
                    break;
                case "delete_playlist_track":
                    input_om = MusicManager.deletePlaylistTrack(input_om);
                    oos.writeObject(input_om);
                    break;
                case "rate_track":
                    input_om = MusicManager.rateTrack(input_om);
                    oos.writeObject(input_om);
                    break;
                case "request_file":
                    //receive track
                    Track track = (Track) input_om.getObject();
                    String title = track.getTitle();
                    System.out.println("Track request, id: " +  track.getId() + " , title: " + title);
                    File file = new File(track.getPath());

                    int file_length = (int)file.length();
                    System.out.println("File size " + file_length);

                    oos.writeInt(file_length);
                    oos.flush();

                    //llegim del fitxer en grups de bytes
                    byte[] buffer = new byte[4096 * 4];

                    InputStream in = new FileInputStream(file);
                    //l'enviem al client
                    OutputStream out = client_socket.getOutputStream();

                    int count;
                    int current = 0;
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                        out.flush();
                        current+= count;
                        System.out.println("Sending track: " + title + " to client. " + current + "/" + file_length);
                    }

                    System.out.println("Track sent.");

                    out.close();
                    in.close();

                    break;
                case "update_playcount":
                    input_om = MusicManager.updatePlaycount(input_om);
                    oos.writeObject(input_om);
                    break;
                case "request_following":
                    input_om = UserManager.requestFollowing(input_om);
                    oos.writeObject(input_om);
                    break;
                case"request_user":
                    input_om = UserManager.requestUser(input_om);
                    oos.writeObject(input_om);
                    break;
                case "follow_user":
                    input_om = UserManager.followUser(input_om);
                    oos.writeObject(input_om);
                    break;

            }
            client_socket.close();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
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

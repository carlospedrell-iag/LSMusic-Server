package Model;

import Model.Entity.ObjectMessage;
import Model.Entity.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                ObjectMessage input_obj = (ObjectMessage)ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(client_socket.getOutputStream());
                //segons el tipus d'operacio que ens diu el Object Message, s'executa un metode diferent
                switch (input_obj.getType()){
                    case "register":
                        ArrayList<String> errors = registerUser((User)input_obj.getObject());
                        input_obj.setErrors(errors);
                        oos.writeObject(input_obj);
                        break;
                }

            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> registerUser(User user){
        ArrayList<String> errors = new ArrayList<String>();

        return errors;
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

package View;

import Model.Database.TrackDAO;
import Model.Entity.Track;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatsPanel extends JPanel {

    private ArrayList<Track> topTenTracks;
    public StatsPanel(){
        refreshTopTen();
    }

    public void refreshTopTen(){
        TrackDAO trackDAO = new TrackDAO();
        this.topTenTracks = trackDAO.findTopTen();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        int offsetX = 47;
        int offsetY = 80;
        int max_i = topTenTracks.size();
        int max_value = 1;

        if(max_i > 0){
            max_value = topTenTracks.get(0).getPlays();
        } else {
            g.setColor(new Color(64, 64, 64));
            g.drawString("No Tracks Yet",20,20);
        }

        if(max_value < 1 ){
            max_value = 1;
        }


        for (int i = 0; i < max_i ; i++) {
            int value = topTenTracks.get(i).getPlays();
            //mesurem els valors relatiu al tamany de la finestra
            int x = (i * (getWidth()/11)) + offsetX;
            int y = getHeight() - ( (value  * getHeight()) /max_value) + offsetY;
            int width = getWidth()/ (offsetX - 20);
            int height = getHeight() - y - 50;
            //quan arriba al limit de petit, la barra mesurará 1px d'alçada
            if(height < 1){
                y = getHeight() - 50;
                height = getHeight() -  y - 51;
            }

            g.setColor(new Color(171, 171, 171));
            g.fillRect(x,y,width,height);
            g.setColor(new Color(64, 64, 64));
            g.drawString("" +value + " Times",x,y - 10);
            g.drawString(topTenTracks.get(i).getTitle(),x,y - 30);
        }
    }
}

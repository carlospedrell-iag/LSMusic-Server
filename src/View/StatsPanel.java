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
        int max = topTenTracks.get(0).getPlays();
        int min = topTenTracks.get(9).getPlays();
        System.out.println("max: " + max);

        for (int i = 0; i < 10 ; i++) {
            int value = topTenTracks.get(i).getPlays();
            g.setColor(Color.BLACK);

            int x = (i * (getWidth()/11)) + offsetX;
            int y = getHeight() - ( (value  * getHeight()) /max) + offsetY;
            int width = getWidth()/ (offsetX - 20);
            int height = getHeight() - y - 50;
            if(height < 1){
                y = getHeight() - 50;
                height = getHeight() -  y - 51;
            }

            System.out.println("getHeight: " + getHeight() + " value: " + value + " max: " + max);
            System.out.println("y: " + y);
            System.out.println("height: " + height);

            g.setColor(new Color(171, 171, 171));
            g.fillRect(x,y,width,height);
            g.setColor(new Color(64, 64, 64));
            //g.setFont(new Font("Arial", Font.PLAIN,12));
            g.drawString("" +value + " Times",x,y - 10);
            g.drawString(topTenTracks.get(i).getTitle(),x,y - 30);
        }
    }
}

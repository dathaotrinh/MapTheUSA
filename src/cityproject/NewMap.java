package cityproject;

import java.awt.*;
import java.util.Stack;
import javax.swing.*;

public class NewMap extends Canvas {

    City[] cities = new City[200];  //array of cities (Vertices) max = 200
    int cityCount;                  // actual number of cities
    Edge[] links = new Edge[2000];  // array of links  (Edges)  max = 2000
    int linkCount;                  // avtual number of links
    City source;
    Stack<City> s;
    
    NewMap() {

    } // end CityCanvas(...)    

    NewMap(int cCount, City[] c, int lCount, Edge[] l, City source, Stack<City> s) {

        this.cities = c;
        this.cityCount = cCount;
        this.links = l;
        this.linkCount = lCount;
        this.source = source;
        this.s = s;
    } // end CityCanvas(...)    

    public void paint(Graphics graphics) {

        // fill background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 1500, 900);

        // place us image under map
        Image us = new ImageIcon("us.png").getImage();
        graphics.drawImage(us, 125, 0, null);

        // draw links
        // iterate link array
        for (int i = 0; i < linkCount; i++) {

            // get soucrce city  and destination city coordinates
            int xS = links[i].getSource().getX();       // x coordinate of source city
            int yS = links[i].getSource().getY();       // y coordinate of source city
            int xD = links[i].getDestination().getX();	// x coordinate of destination city
            int yD = links[i].getDestination().getY();  // y coordinate of destination city

            graphics.setColor(new Color(200, 200, 200));
            graphics.drawLine(xS, yS, xD, yD);
        } // end for

        // draw links from 2 cities 
        int xS = source.getX();
        int yS = source.getY();
        for(int i = s.size() - 1; i >= 0; i--)
        {            
            City des = s.get(i);
            
            int xD = des.getX();
            int yD = des.getY();
            
            graphics.setColor(Color.red);
            graphics.drawLine(xS, yS, xD, yD);      
            
            City newSource = des;   
            xS = newSource.getX();
            yS = newSource.getY();
        }
        // draw cities
        for (int i = 0; i < cityCount; i++) {
            //draw a dot for each city, 4 x 4 pixels
            graphics.setColor(Color.red);
            graphics.fillOval(cities[i].getX() - 3, cities[i].getY() - 3, 6, 6);

        } // end for

        // draw labels
        for (int i = 0; i < cityCount; i++) {
            // draw a label for each city, offest by 6 hor. and 9 ver. pixels
            graphics.setColor(Color.black);
            graphics.setFont(new Font("Lucida Console", Font.BOLD, 9));
            graphics.drawString(cities[i].getName(), cities[i].getX() + 6, cities[i].getY() + 9);
        } // end for

        // add note to the canvas
        Image logo = new ImageIcon("note.png").getImage();
        graphics.drawImage(logo, 35, 600, null);

    }  // end paint()

    
} // end class CityMap
//**********************************************************************************************************************************

/* CityProject.java  -- project class file with the project's main method   
 *  
 * This software package creates a graph of cities in the Unitied States with
 * links between the cities. Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph.   The data for the cities and
 * links are read into arrays from data files, which should be in the project folder.
 * The files are CSV files, which can be read and edited in Excel.
 *
 * The main class for the project is the CityProject class.   Other class include:
 * 
 *   Vertex - clas for each Vertex in a graph.
 *   City extends Vertex - Each City is a Vertex with added properties.  Each City
 *      has a unique name, and X and Y cooordinates for location on a 1500 by 900 Canvas.
 *   Edge - an edge in the graph, with a source, destination, and length.
 *   AjacencyNode - a node for a linked list of cities directly connected to each City.
 *      Each City has a linked list of adjacnt cities, created from the info in the 
 *      data files, with destination City and distance data in the node, and a 
 *      link to the next node. 
 *   CityMap - extends Canvas, a map of the graph on a 1500 by 900 GUI Canvas.
 *      A CityMap object in instantiated in the drawMap method in the CityProject class.
 * 
 * The main method in the CityProject class calls methods to reads City and Edge 
 * data from data files into arrays, set up the adjacency list in each instance 
 * of City, print a list of Vertex cities and their Edges, then draw a map of the graph.
 *
 */

package cityproject;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import marytts.exceptions.SynthesisException;

public class CityProject {

    // set up a JFrame to hold the canvas
    static JFrame frame = new JFrame();
   
    // main metod for the project
    public static void main(String[] args) throws IOException, SynthesisException {

        City[] cities = new City[200]; //array of cities (Vertices) max = 200
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new City();
        }

        Edge[] links = new Edge[2000];// array of links  (Edges)  max = 2000
        for (int i = 0; i < links.length; i++) {
            links[i] = new Edge();
        }

        int cityCount; //    actual number of cities
        int linkCount; //    actual number of links

        // load cities into an array from a datafile
        cityCount = readCities(cities);

        // load links into an array from a datafile
        linkCount = readLinks(links, cities);

        // create the adjacency list for each city based on the link array
        createAdjacencyLists(cityCount, cities, linkCount, links);

        // print adjacency lists for all cities
        PrintAdjacencyLists(cityCount, cities);

        // draw a map of the cities and links
        drawMap(cityCount, cities,linkCount, links);
               
        // search for shortest path between two cities        
        mapShortestPath(cities, cityCount, links, linkCount);
        
                
    } // end main
    //************************************************************************
    
    // this method uses Dijkstra's algorithm to find the shortest path from city to city
    public static void mapShortestPath(City[] cities, int cityCount, Edge[] links, int linkCount) throws IOException, SynthesisException{
        
        // ask User to input where they are 
        // and where they are heading to
        Scanner keyboard = new Scanner(System.in);
        System.out.print("\nWhere are you right now? ");
        String start = keyboard.nextLine();  
        System.out.print("\nWhere are you going? ");
        String end = keyboard.nextLine();
        
        System.out.println();
        
        // find the city the user wants to arrive at
        // and assign it to City object named destination
        City destination = findCity(cities, end);  
        
        // find the city at which the user currently is
        // and assign it to City object named source
        City source = findCity(cities, start);
        
        // set current city pointer to point to the source city
        City current = source;        
        
        // declare a genetic stack to store all the cities in
        Stack<City> unVisited = new Stack();
        
        // for loop to iterate through the city's list
        for(int i = 0; i < cityCount; i++)
        {           
           // add all the cities into the stack named unVisited 
           unVisited.add(cities[i]);            
        }
        
        // set bestDistance property of current city to be zero
        current.setBestDistance(0);
        
        // set immediatePredecessor property of current city to be null
        current.setImmediatePredecessor(null);
        
        // for each vetex adjacent to current, 
        // determine if there is a new shortest path to vertices adjacent to current city through current city
        while(!unVisited.isEmpty())
        {
            for(AdjacencyNode i: neighbor(current, cityCount, cities, cityLinkCount(current, links, linkCount)))
            {                
                updateBestDistance(i, current);
            }
            
            // set visited property of current city to be true
            current.setVisited(true);
            
            // take the current city out of the stack
            unVisited.remove(current); 
            
            // assign infinity to updatedDistance variable
            // this variable will be updated later on 
            // when we compare to city's best distance
            int updatedDistance = Integer.MAX_VALUE;
            
            // iterate through the whole list of unVistied set
            Iterator<City> ite = unVisited.iterator();
            
            // loop through the list until it reaches the end
            while(ite.hasNext())
            {
                // assign each value in the list to City object
                City city = ite.next();
                // update new best distance for each city
                // If there is a new shortest path to a vertex through the current,
                // update its bestDistance and immediatePredecessor values
                if(city.getBestDistance() < updatedDistance && !city.getVisited())
                {
                    updatedDistance = city.getBestDistance();
                    current = city;
                }
            }
        }

        // and assign destination city to City object named temp        
        City temp = findCity(cities, destination.getName());
             
        // declare a new stack
        Stack<City> s = new Stack<>();
        
        while(!temp.getName().equalsIgnoreCase(source.getName()))
        {
            // Starting at the destination, add immediatePredecessor vertex in turn to a stack
            s.push(temp);
            
            // update temp to be the immediatePredecessor of the previous city
            temp = temp.getImmediatePredecessor();
        }
                              
        System.out.println("Here is the shortest path\n");
        System.out.println("From: " + source.getName());

        drawMap2(cityCount, cities,linkCount, links, source, s);

        // remove the items from the stack to obtain the shortest path from the source to the destination.
        for(int i = s.size() - 1; i >= 0; i--)
        {
            City removedCity = s.get(i);
            System.out.println("        to: " + removedCity.getName() 
                                + " distance: " + removedCity.getBestDistance());
        }
        
        VoiceAssistant voice = new VoiceAssistant();

		
        voice.speakUp("The shortest distance from " + source.getName()
                + " to " + s.get(0).getName() + " is " + s.get(0).getBestDistance());
        
    }
    
    private static City[] addEdges(City c)
    {
        ArrayList<City> path = new ArrayList<>();
        path.add(c);
        
        return path.toArray(new City[path.size()]);
    }
    
    
    
        
    // update new value for bestDistance property of each city
    // and update new immediate predecessor for each city
    public static void updateBestDistance(AdjacencyNode i, City current){
        // calculate the sum of each city's distance in adjacency list
        // and current city's best distance
        int newCost = i.getDistance() + current.getBestDistance();
        if(i.getCity().getBestDistance()  > newCost)
        {
            i.getCity().setBestDistance(newCost);
            i.getCity().setImmediatePredecessor(current);
        }
    }
    
    // this method is to count the number of city that is adjacent to the current city
    public static int cityLinkCount(City current, Edge[] links, int linkCount){
        // declare a stack
        Stack s = new Stack();
        
        // loop through the array of edges
        for(int i = 0; i < linkCount; i++)
        {
            // if the name of source city in the array element 
            // is equal to the name of the current city
            if(links[i].getSource().getName().equals(current.getName()))
            {
                // add the destination city in the array element to stack
                s.add(links[i].getDestination());
            }
        }
        // return the stack's size
        return s.size();
    }

    // neighbor method returns an array of adjacency nodes
    // this method is used to get a list of adjacent cities to the current city
    public static AdjacencyNode[] neighbor(City current, int cityCount, City[] cities, int cityLinkCount){
        // declare an AdjacencyNode array with a length of cityLinkCount
        AdjacencyNode[] neighbor = new AdjacencyNode[cityLinkCount];
        // loop through each city
        for(int i = 0; i < cityCount; i++)
        {
            // compare each city's name and the name of current city
            // ignore case for the string
            if(cities[i].getName().equalsIgnoreCase(current.getName()))
            {
                // initialize j to be 0, j is used as a loop counter for while loop below
                int j = 0;
                // get the first adjacent city and assign it to cur
                AdjacencyNode cur = cities[i].getAdjacencyListHead();
                // while cur is not empty
                while(cur != null)
                {
                    // assign cur to neighbor array
                    neighbor[j] = cur;
                    // increment j by one
                    j++;
                    // point to the next element
                    cur = cur.getNext();
                }break;  // stop for loop after it can get into if and while loop           
            }
        }
        
        // return the array
        return neighbor;

    }

    
    // method to read city data into an array from a data file
    public static int readCities(City[] cities) {

        int count = 0; // number of cities[] elements with data

        String[][] cityData = new String[123][3]; // holds data from the city file
        String delimiter = ",";                   // the delimiter in a csv file
        String line;                              // a String to hold each line from the file
        
        String fileName = "cities.csv";           // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there is another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                cityData[count] = line.split(delimiter);
                
                // read data from the 2D array into an array of City objects
                cities[count].setName(cityData[count][0]);
                cities[count].setX(Integer.parseInt(cityData[count][1]));
                cities[count].setY(Integer.parseInt(cityData[count][2]));

                count++;
            }// end while

            infile.close();

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;

    } // end loadCities()
    //*************************************************************************

    // method to read link data into an array from a data file
    public static int readLinks(Edge[] links, City[] cities) {
        int count = 0; // number of links[] elements with data

        String[][] CityLinkArray = new String[695][3]; // holds data from the link file
        String delimiter = ",";                       // the delimiter in a csv file
        String line;				      // a String to hold each line from the file

        String fileName = "links.csv";                // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();
                

                // split the line into separate objects and store them in a row in the array
                CityLinkArray[count] = line.split(delimiter);

                // read link data from the 2D array into an array of Edge objects
                // set source to vertex with city name in source column
                links[count].setSource(findCity(cities, CityLinkArray[count][0]));
                // set destination to vertex with city name in destination column
                links[count].setDestination(findCity(cities, CityLinkArray[count][1]));
                //set length to integer valuein length column
                links[count].setLength(Integer.parseInt(CityLinkArray[count][2]));

                count++;

            }// end while

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    } // end loadLinks()
    //*************************************************************************

    // memthod to find the City object with the given city name
    public static City findCity(City[] cities, String n) {
        int index = 0;  // loop counter
        // go through the cities array until the name is found
        // the name will be in the list
        
        while (!cities[index].getName().equalsIgnoreCase(n)) {

            // increment index by one
            index++;
            
            // if the city is not found in the list
            if(cities[index].getName() == null){
                
                // stop the loop
                break;
            }
        }// end while()
        
        if(cities[index].getName() == null){
            throw new NoSuchElementException("No Such City Found");             
            } else{
            return cities[index];
        }
    } // end  findCity()
    
    

// method to create an adjacency lists for each city
    public static void createAdjacencyLists(int cityCount, City[] cities, int linkCount, Edge[] links) {

        AdjacencyNode temp = new AdjacencyNode();

        // iterate city array
        for (int i = 0; i < cityCount; i++) {

            //iterate link array
            for (int j = 0; j < linkCount; j++) {
                // if the currentl link's source is the current city
                if (links[j].getSource() == cities[i]) {

                    /* create a node for the link and inseert it into the adjancency list
                     * as the new head of the list. 
                     */
                    // temporarily store the current value of the list's head
                    temp = cities[i].getAdjacencyListHead();
                    //create a new node
                    AdjacencyNode newNode = new AdjacencyNode();
                    // add city and distance data
                    newNode.setCity(links[j].getDestination());
                    newNode.setDistance(links[j].getLength());
                    // point newNode to the previous list head
                    newNode.setNext(temp);

                    // set the new head of the list to newNode
                    cities[i].setAdjacencyListHead(newNode);

                }  // end if
            } // end for j
        } // end for i

    } // end createAdjacencyLists()

    // method to print adjacency lists
    public static void PrintAdjacencyLists(int cityCount, City[] cities) {

        System.out.println("List of Edges in the Graph of Cities by Source City");
        // iterate array of cities
        for (int i = 0; i < cityCount; i++) {

            // set current to adjacency list for this city    
            AdjacencyNode current = cities[i].getAdjacencyListHead();

            // print city name
            System.out.println("\nFrom " + cities[i].getName());

            // iterate adjacency list and print each node's data
            while (current != null) {
                System.out.println("\t"+ current.toString());
                current = current.getNext();
            } // end while (current != null) 

        }   // end for i 

    } // end PrintAdjacencyLists()
   
    // method to draw the graph (map of cities and links)
   static void drawMap(int cCount, City[] c, int lCount, Edge[] l)
   {
       CityMap canvas1 = new CityMap(cCount,  c, lCount, l);
       
        int width = 1500; // width of the Canvas
        int height = 900; // heightof the Canvas 
        frame.setAlwaysOnTop(true);
        
        frame.setTitle("U.S. Cities");
        frame.setSize(width, height);
        frame.setLocation(10, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the canvas to the frame as a content panel
        frame.getContentPane().add(canvas1);
        frame.setVisible(true);
   } // end drawMap() 
    
  static void drawMap2(int cCount, City[] c, int lCount, Edge[] l, City source, Stack<City> s)
   {
        NewMap canvas = new NewMap(cCount,  c, lCount, l, source ,s);
        //frame.setAlwaysOnTop(true);
        frame.getContentPane().removeAll();
        
        // add the canvas to the frame as a content panel
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
   } // end drawMap2() 
        
    
} // end class cityProject

# City Project / Map The USA


## Overview

 * This software package creates a graph of cities in the Unitied States with links between the cities, then finds the shortest path from one city to another using Dijkstra's Algorithm. 

## Features

 * Using Dijkstra's algorithm to trace the shortest path from one place to another
```
Dijkstra’s algorithm requires three properties for each vertex in the graph:

  *	visited – whether or not the vertex has been “visited” yet in our algorithm 
    (boolean, initialized to false)
  *	bestDistance – the best distance found so far from the source to the vertex 
    (int, initialized to infinity or MAX_INT)
  *	immediatePredecessor – the immediate predecessor to this vertex along the shortest path
    (initialized to null)
    
This information could be stored as properties of an object for each vertex, 
or stored in parallel arrays, where the index of each array element is the number of the vertex.

Additionally, a set U of all unvisited vertices should be initialized to include all vertices.

    1.	Set the currentVertex to be the source vertex. Set its bestDistance to be zero, 
        and leave its immediatePredecessor null.
        
    2.	While (U is not empty)
        {
            a.	For each vetex adjacent to currentVertex, 
                determine if there is a new shortest path to vertices adjacent to currentVertex 
                through currentVertex.
                
                    If there is a new shortest path to a vertex through the currentVertex
                        update its bestDistance and immediatePredecessor values 
                        (This step is referred to as “relaxation”.  
                        We are updating the shortest path for vertices adjacent to the current vertex 
                        if there is a new shortest path from the source to a vertex 
                        through the current vertex.)
                        
            b.	Mark currentVertex as visited.
            
            c.	update currentVertex to be the vertex from U with the shortest bestDistance.
         }
    3.	Starting at the destination, add immediatePredecessor vertex in turn to a stack.
     
    4.	Remove the items from the stack to obtain the shortest path from the source to the destination.

Once all vertices in a graph have been visited, 
steps 3 and 4 in the above algorithm can be repeated for each vertex in the graph 
to solve the single source, all destinations shortest paths.
```

 * Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph.  
 * The data for the cities and links are read into arrays from data files.
 * The files are CSV files, which can be read and edited in Excel. 
 * Using Object-oriented design to manage the data files.
 * Using FreeTTS library to read the result out loud. 

 
## Explores

#### Click on the image to see the video
 
 [![image](https://github.com/jtrinh21/MapTheUSA/blob/master/Screenshot%20(1).png)](https://www.youtube.com/watch?v=iRK5IX6ztlM&feature=youtu.be)

   

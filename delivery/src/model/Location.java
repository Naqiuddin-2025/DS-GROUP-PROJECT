package model;

/**
 * =============================================================
 *  Location.java  |  model package
 *  PURPOSE : Represents one node (vertex) in the delivery map graph.
 *
 *  ROLE IN DIJKSTRA:
 *    - Every city junction, restaurant, and customer address
 *      is stored as a Location object.
 *    - During Dijkstra, each Location tracks:
 *        shortestDist  → best known distance from the source
 *        visited       → whether it has been fully processed
 *
 *  GRAPH VISUALISATION:
 *
 *   [Restaurant] ──5km── [JunctionA] ──3km── [Customer]
 *        └──────────9km──── [Mall] ───4km── [JunctionB]
 *                                               │
 *                                             1km
 *                                               │
 *                                          [Customer]
 *
 *  Each box = one Location object.
 *
 *  OOP CONCEPTS USED:
 *    - Encapsulation : private fields, public getters/setters
 * =============================================================
 */
public class Location {

    //instance variables
    private String  locationId;   
    private String  description;

    //used by djikstras
    private double  shortestDist;  // best distance found so far from source
    private boolean visited;       // true once this node is fully processed

    //contructors
    public Location(String locationId, String description) {
        this.locationId   = locationId;
        this.description  = description;
        this.shortestDist = Double.MAX_VALUE; //limit unreacheable (represent infinity)
        this.visited      = false;
    }
    
    //getters
    public String  getLocationId(){ 
        return locationId; 
    }
    public String  getDescription(){
        return description; 
    }
    public double  getShortestDist(){
        return shortestDist;
    }
    public boolean isVisited(){ 
        return visited; 
    }

    //setters (used by DeliveryOptimizer during Dijkstra)
    public void setShortestDist(double distance){ 
        this.shortestDist = distance;
    }
    public void setVisited(boolean visited){ 
        this.visited = visited;
    }

    // reset to its initial location
    public void reset() {
        this.shortestDist = Double.MAX_VALUE;
        this.visited      = false;
    }

    //toString
    @Override
    public String toString() {
        return locationId + " - " + description;
    }
}
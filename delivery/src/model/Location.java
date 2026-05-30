package model;
public class Location {

    //instance variables
    private String  locationId;   
    private String  description;
    private Location previousLocation;

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
    public Location getPreviousLocation() {
    return previousLocation;
    }

    //setters (used by DeliveryOptimizer during Dijkstra)
    public void setShortestDist(double distance){ 
        this.shortestDist = distance;
    }
    public void setVisited(boolean visited){ 
        this.visited = visited;
    }
    public void setPreviousLocation(Location previousLocation) {
    this.previousLocation = previousLocation;
    }

    // reset to its initial location
    public void reset() {
        this.shortestDist = Double.MAX_VALUE;
        this.visited      = false;
        this.previousLocation = null;
    }

    //toString
    @Override
    public String toString() {
        return locationId + " - " + description;
    }
}
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import model.Location;
import model.Rider;

public class DeliveryOptimizer {

    //priority queue (Min Heap)
    private PriorityQueue<Rider> riderQueue;

    //store all locations
    private ArrayList<Location> locations;

    //graph representation
    //key = source location
    //value = list of connected edges
    //return all roads connected to location
    //string = location id
    //arraylist<edge> = all roads leaving location 
    private HashMap<String, ArrayList<Edge>> graph;

    //represents a road connection
    private class Edge {

        private String destination;
        private double distance;

        public Edge(String destination, double distance) {
            this.destination = destination;
            this.distance = distance;
        }

        public String getDestination() {
            return destination;
        }

        public double getDistance() {
            return distance;
        }
    }

    //constructor
    public DeliveryOptimizer() {

        riderQueue = new PriorityQueue<>();
        locations = new ArrayList<>();
        graph = new HashMap<>();
    }

    //RIDER ASSIGNMENT
    
    //add rider to priority queue
    public void addRider(Rider rider) {
        riderQueue.offer(rider);
    }

    //return assigned rider
    public Rider assignRider() {
        return riderQueue.poll();
    }

    //displays every rider currently inside the priority queue.
    public void displayAvailableRiders() {
        for (Rider rider : riderQueue) { 
            System.out.println(rider);
        }
    }

    //LOCATION MANAGEMENT (graph)
    public void addLocation(Location location) {
        locations.add(location);
        graph.put(location.getLocationId(),new ArrayList<Edge>());  //creates an empty edge list (only vertices)
    }

    public void displayLocations() {
        for (Location location : locations) {
            System.out.println(location);
        }
    }

    //GRAPH METHODS
    
    //creates a road between two locations
    public void addRoad(String source, String destination, double distance) {
        graph.get(source).add(new Edge(destination, distance)); //connects the source and destination graph wiht a weighted edge
        graph.get(destination).add(new Edge(source, distance)); //both directions (two way road)
    }

    public void displayRoads() {
        for (String source : graph.keySet()) {
            System.out.print(source + " -> ");
            ArrayList<Edge> edges = graph.get(source);  //all connected roads to source are stored in edges
            for (Edge edge : edges) {  //loop through all connected roads to source
                System.out.print(edge.getDestination()+"("+edge.getDistance()+"km) ");
            }
            System.out.println();
        }
    }

    //HELPER METHOD
    
    //Find Location Object
    private Location getLocation(String locationId) {
        for (Location location : locations) {
            if (location.getLocationId().equals(locationId)) {
                return location;
            }
        }
        return null;
    }

    //reset dijkstra variables so can use again after djikstra edited them
    private void resetLocations() {
        for (Location location : locations) {
            location.reset();
        }
    }

    // DIJKSTRA ALGORITHM
    // Returns shortest distance
    public double findShortestPath(String sourceId,String destinationId) {
        resetLocations();
        Location source = getLocation(sourceId);
        if (source == null) {
            return -1;
        }

        source.setShortestDist(0);

        while (true) {

            Location current = null;  //the next location Dijkstra wants to process

            double smallestDistance = Double.MAX_VALUE;

            // Find nearest unvisited node
            for (Location location : locations) {
                if (!location.isVisited() && location.getShortestDist()< smallestDistance) {
                    smallestDistance = location.getShortestDist();
                    current = location;
                }
            }

            if (current == null) {
                break;
            }

            current.setVisited(true);

            ArrayList<Edge> neighbours = graph.get(current.getLocationId());

            for (Edge edge : neighbours) {

                Location neighbour = getLocation(edge.getDestination());

                if (!neighbour.isVisited()) {

                    double newDistance = current.getShortestDist() + edge.getDistance();

                    if (newDistance < neighbour.getShortestDist()) {

                        neighbour.setShortestDist(newDistance);

                        neighbour.setPreviousLocation(current);
                    }
                }
            }
        }
        Location destination = getLocation(destinationId);
        return destination.getShortestDist();
    }

    // Display Route
    public void displayShortestRoute(String destinationId) {
        
        Location destination = getLocation(destinationId);

        if (destination == null) {
            System.out.println("Location not found");
            return;
        }

        ArrayList<String> route = new ArrayList<>();
        Location current = destination;
        while (current != null) {
            route.add(0,current.getLocationId());
            current = current.getPreviousLocation();
        }

        System.out.println("\nShortest Route:");

        for (int i = 0;i < route.size();i++) {
            System.out.print(route.get(i));
            if (i < route.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
package model;

public class Rider implements Comparable<Rider> {

    //instance variables
    private String  riderId;
    private String  riderName;
    private double  estimatedDeliveryTime;  // Used for rider priority comparison
    private double  distanceKm;             // km from restaurant
    private boolean isAvailable;

    //constructor
    public Rider(String riderId, String riderName, double estimatedDeliveryTime, double distanceKm) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.distanceKm = distanceKm;
        this.isAvailable = true;   // riders start available
    }

    //getters
    public String getRiderId(){
       return riderId;
    }
    
    public String getRiderName(){
       return riderName; 
    }
    
    public double getEstimatedDeliveryTime(){
       return estimatedDeliveryTime;
    }
    public double getDistanceKm(){
       return distanceKm; 
    }
    public boolean isAvailable(){
       return isAvailable; 
    }

    //Setters
    public void setAvailable(boolean available){ 
       this.isAvailable = available;
    }
    public void setEstimatedDeliveryTime(double minutes){
        this.estimatedDeliveryTime = minutes;
    }
    
    //compare the delivery time for both riders and check which one is smaller(smaller becomes the root of heap map) [r1<r2 = negative] [r1>r2 = positive] [r1=r2 = 0]
    @Override
    public int compareTo(Rider other) {
        return Double.compare(this.estimatedDeliveryTime,other.estimatedDeliveryTime);
    }

    //toString (standardize the formats)
    @Override
    public String toString() {
         
        String availability;
        
        if (isAvailable) {
        availability = "Available";
        }
        else {
        availability = "Busy";
        }

        return String.format("Rider[%-4s | %-12s | %5.1f min | %4.1f km | %s]", riderId, riderName, estimatedDeliveryTime, distanceKm, availability); //for availability can use ternary operator
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Naqiuddin
 */
public class User {
    
    private String userID;
    private String name;
    private String address;
    
    public User (String userID, String name){
        this.userID = userID;
        this.name = name;        
    } 
    
    public String getUserID(){
        return userID;
    } 
    public String getName(){
        return name;
    }
    
}

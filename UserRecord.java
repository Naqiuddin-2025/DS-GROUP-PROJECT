package Database;

/**
 *
 * @author Naqiuddin
 */
public class UserRecord {
    
    private String userID;
    private String lastOrderDate;
    
    public UserRecord (String userID, String lastOrderDate) {
        this.userID = userID;
        this.lastOrderDate = lastOrderDate;
    }    
}

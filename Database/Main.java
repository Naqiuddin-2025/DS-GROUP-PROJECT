package Database;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        
        // You can now directly access the other classes
        User u1 = new User("U001", "Naqiuddin");
        db.addUser(u1);
        
        System.out.println("System Initialized. User added: " +db.getUser("U001").getName());
    }
}

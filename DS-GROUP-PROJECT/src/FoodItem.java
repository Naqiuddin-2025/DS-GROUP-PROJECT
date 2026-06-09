public class FoodItem {
    private final String name;
    private final double price;
    private final String category;

    //constructor to initialize food item properties
    public FoodItem(String name, double price, String category) {
        this.name = name.trim();
        this.price = price;
        this.category = category.trim();
    }

    //getters (useful for system integration later)
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    //formats the output into a clean, aligned tabular row
    @Override
    public String toString() {
        return String.format("| %-20s | %-12s | $%-8.2f |", name, category, price);
    }
}
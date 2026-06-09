import java.util.Scanner;

public class MenuUI {
    private final SearchEngine engine;
    private final Scanner input;

    public MenuUI() {
        this.engine = new SearchEngine();
        this.input = new Scanner(System.in);
        loadDefaultMenuData(); //automatically seeds data for demonstration purposes
    }

    //seeds data to build a working, balanced sample tree structure
    private void loadDefaultMenuData() {
        engine.insert(new FoodItem("Pizza Margherita", 12.99, "Italian"));
        engine.insert(new FoodItem("Burger Deluxe", 8.49, "Fast Food"));
        engine.insert(new FoodItem("Sushi Roll", 15.99, "Japanese"));
        engine.insert(new FoodItem("Almond Salad", 9.99, "Healthy"));
        engine.insert(new FoodItem("Taco Supreme", 7.99, "Mexican"));
    }

    public void launchMenu() {
        int userChoice = 0;

        while (userChoice != 3) {
            System.out.println("GOODTECH FOOD MANAGEMENT INTERFACE");
            System.out.println("1. View Menu (Alphabetically Sorted via BST)");
            System.out.println("2. Search for a Specific Food Item");
            System.out.println("3. Exit Engine Module");
            System.out.print("Please enter an option (1-3): ");

            try {
                if (input.hasNextInt()) {
                    userChoice = input.nextInt();
                    input.nextLine(); //clear scanner buffering
                } else {
                    System.out.println("\nInput Error: Please enter a valid number integer choice.");
                    input.nextLine(); //discard bad input characters
                    continue;
                }
            } catch (Exception e) {
                System.out.println("An unexpected layout error occurred.");
                continue;
            }

            switch (userChoice) {
                case 1:
                    System.out.println("\n[SYSTEM] Fetching menu items sorted via BST...");
                    engine.displayMenu();
                    break;

                case 2:
                    System.out.print("\nEnter the name of the dish you want to find: ");
                    String searchQuery = input.nextLine();

                    //performance Benchmark: Capturing algorithm execution time
                    long calculationStartTime = System.nanoTime();
                    FoodItem matchResult = engine.search(searchQuery);
                    long calculationEndTime = System.nanoTime();

                    if (matchResult != null) {
                        System.out.println("\nMatch Successfully Located!");
                        System.out.println(matchResult);
                        System.out.println("Algorithmic Search Speed: " + (calculationEndTime - calculationStartTime) + " nanoseconds.");
                    } else {
                        System.out.println("\nSorry, '" + searchQuery + "' could not be found on our active menu.");
                    }
                    break;

                case 3:
                    System.out.println("\n[SYSTEM] Shutting down Search Module engine safely.");
                    break;

                default:
                    System.out.println("\n Invalid Selection. Please choose numbers between 1 and 3.");
            }
        }
    }
}
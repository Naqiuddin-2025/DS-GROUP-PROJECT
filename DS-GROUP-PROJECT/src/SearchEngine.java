class BSTNode {
    FoodItem data;
    BSTNode left, right;

    public BSTNode(FoodItem data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class SearchEngine {
    private BSTNode root;

    public SearchEngine() {
        this.root = null;
    }
    //OPERATION 1: Alphabetical Insertion Logic-O(log n)
    public void insert(FoodItem item) {
        if (item == null) return;
        root = insertRecursive(root, item);
    }

    private BSTNode insertRecursive(BSTNode current, FoodItem item) {
        //base case: found an empty spot in the tree
        if (current == null) {
            return new BSTNode(item);
        }

        //compare names case-insensitiveto establish alphabetical sorting
        int comparison = item.getName().compareToIgnoreCase(current.data.getName());

        if (comparison < 0) {
            current.left = insertRecursive(current.left, item);
        } else if (comparison > 0) {
            current.right = insertRecursive(current.right, item);
        }
        //if comparison == 0, the item name already exists(skipping duplicates)

        return current;
    }
    //OPERATION 2: highly Optimized Search Lookup-O(log n)
    public FoodItem search(String targetName) {
        if (targetName == null || targetName.trim().isEmpty()) {
            return null;
        }
        return searchRecursive(root, targetName.trim());
    }

    private FoodItem searchRecursive(BSTNode current, String targetName) {
        //base cases: tree is empty or item is located
        if (current == null || current.data.getName().equalsIgnoreCase(targetName)) {
            return (current != null) ? current.data : null;
        }

        int comparison = targetName.compareToIgnoreCase(current.data.getName());

        if (comparison < 0) {
            return searchRecursive(current.left, targetName); // Search left subtree
        }
        return searchRecursive(current.right, targetName); // Search right subtree
    }
    //OPERATION 3:alphabetical Sorted Display (In-Order)-O(n)
    public void displayMenu() {
        if (root == null) {
            System.out.println("|The menu is currently empty.|");
            return;
        }
        
        printHeader();
        inOrderTraversal(root);
        printFooter();
    }

    private void inOrderTraversal(BSTNode current) {
        if (current != null) {
            inOrderTraversal(current.left);//process Left Subtree (A-Z)
            System.out.println(current.data);//process Root Node Data
            inOrderTraversal(current.right);//process Right Subtree (A-Z)
        }
    }

    private void printHeader() {
        System.out.println("+----------------------+--------------+----------+");
        System.out.println("| Food Name            | Category     | Price    |");
        System.out.println("+----------------------+--------------+----------+");
    }

    private void printFooter() {
        System.out.println("+----------------------+--------------+----------+");
    }
}
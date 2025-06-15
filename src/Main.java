import java.util.Map;
import java.util.Scanner;

public class Main {
    static boolean quit = false;
    private static PharmacyManager manager = new PharmacyManager();
    public static void main(String[] args) {
        while(!quit) {
            StartScreen();
        }
    }
    public static void SignUp(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== PHARMACY REGISTRATION ===");
        System.out.println("Enter pharmacy name");
        System.out.print("(press Enter for auto-generated name): ");

        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = manager.generateNextPharmacyName();
            System.out.println("Auto-generated pharmacy name: " + name);
        }

        System.out.print("Enter pharmacy password: ");
        String password = scanner.nextLine();
        if (password.trim().isEmpty()) {
            System.out.println("Password cannot be empty!");
            SignUp();
            return;
        }
        manager.addPharmacy(name, password);
        UserScreen();
    }
    public static void LogIn(){
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n=== PHARMACY LOGIN ===");
            manager.displayAllPharmacies();

            System.out.print("Pharmacy Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean exist = manager.checkPharmacy(name, password);
            if (exist) {
                System.out.println("✅ Login successful!");
                System.out.println("Welcome back, " + name);
                UserScreen();
                break;
            }
            else {
                System.out.println("❌ Invalid pharmacy name or password.");
                System.out.print("Do you want to try again? (Y/N): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }
    }
    public static void addMedicine() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== ADD MEDICINE ===");
        System.out.print("Enter medicine name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Medicine name cannot be empty!");
            addMedicine();
            return;
        }

        System.out.print("Enter quantity to add: ");
        try {
            int quantity = scanner.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity can't be negative!");
                addMedicine();
                return;
            }

            PharmacyManager.currentPharmacy.addMedicine(name, quantity);
            manager.saveInventory(PharmacyManager.currentPharmacy);
            System.out.println("✅ " + name + " added successfully!");
            System.out.println("New quantity: " + PharmacyManager.currentPharmacy.getQuantity(name));
        }
        catch (Exception e) {
            System.out.println("❌ Invalid entry. Please enter a valid number.");
            scanner.nextLine();
            addMedicine();
            return;
        }
        UserScreen();
    }
    public static void dropMedicine() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== REMOVE MEDICINE ===");

        if (PharmacyManager.currentPharmacy.getInventory().isEmpty()) {
            System.out.println("No medicines in the inventory!");
            UserScreen();
            return;
        }

        System.out.print("\nEnter medicine name: ");
        String name = scanner.nextLine().trim();

        if (!PharmacyManager.currentPharmacy.getInventory().containsKey(name)) {
            System.out.println("❌ " + name + " not found in the inventory!");
            UserScreen();
            return;
        }

        int currentQuantity = PharmacyManager.currentPharmacy.getQuantity(name);
        System.out.println("Current quantity of " + name + " : " + currentQuantity);
        System.out.print("Enter quantity to remove: ");

        try {
            int quantity = scanner.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity can't be negative!");
                dropMedicine();
                return;
            }

            PharmacyManager.currentPharmacy.dropMedicine(name, quantity);
            manager.saveInventory(PharmacyManager.currentPharmacy);

            int newQuantity = PharmacyManager.currentPharmacy.getQuantity(name);
            System.out.println("✅ Medicine removed successfully!");
            System.out.println("New quantity: " + newQuantity);

            if (newQuantity < manager.getWarningThreshold()) {
                System.out.println("⚠️  WARNING: " + name + " is below threshold (" + manager.getWarningThreshold() + ")!");
            }

        }
        catch (Exception e) {
            System.out.println("❌ Invalid entry. Please enter a valid number.");
            scanner.nextLine();
            dropMedicine();
            return;
        }
        UserScreen();
    }
    public static void countMedicine() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== INVENTORY COUNT ===");

        if (PharmacyManager.currentPharmacy.getInventory().isEmpty()) {
            System.out.println("No medicines in the inventory!");
        }
        else {
            System.out.println("Current inventory of " + PharmacyManager.currentPharmacy.getName() + ":");
            System.out.println("----------------------------------------");

            int totalMedicines = 0;
            for (Map.Entry<String, Integer> entry : PharmacyManager.currentPharmacy.getInventory().entrySet()) {
                String status = entry.getValue() < manager.getWarningThreshold() ? " ⚠️ LOW" : " ✅ OK";
                System.out.printf("%-20s : %3d%s%n", entry.getKey(), entry.getValue(), status);
                totalMedicines += entry.getValue();
            }
            System.out.println("----------------------------------------");
            System.out.println("Total medicines in the inventory: " + totalMedicines);
        }
        System.out.print("Enter medicine name: ");
        String medicineName = sc.nextLine().trim();

        if (!medicineName.isEmpty()) {
            int quantity = PharmacyManager.currentPharmacy.getQuantity(medicineName);
            if (quantity > 0) {
                System.out.println(medicineName + " : " + quantity);
            } else {
                System.out.println(medicineName + " not found in the inventory.");
            }
        }
        UserScreen();
    }
    public static void warning() {
        manager.reloadAllInventories();
        manager.checkSwapWarning();
        UserScreen();
    }
    public static void StartScreen(){
        System.out.println("=========================================");
        System.out.println("🏥 PHARMACY MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        System.out.println(" 1️⃣  Sign Up ");
        System.out.println(" 2️⃣  Log In ");
        System.out.println(" 3️⃣  Quit");
        System.out.println("=========================================");
        System.out.print("Enter your choice (1-3): ");

        Scanner inp = new Scanner(System.in);
        try {
            int operation = inp.nextInt();
            switch (operation){
                case 1: SignUp(); break;
                case 2: LogIn(); break;
                case 3:
                    System.out.println("👋 Thank you for using Pharmacy Management System!");
                    quit = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1, 2 or 3.");
                    StartScreen();
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            inp.nextLine();
            StartScreen();
        }
    }
    public static void UserScreen(){
        System.out.println("\n=========================================");
        System.out.println("🏥 WELCOME " + PharmacyManager.currentPharmacy.getName().toUpperCase());
        System.out.println("=========================================");
        System.out.println(" 1️⃣  Add Medicine");
        System.out.println(" 2️⃣  Remove Medicine");
        System.out.println(" 3️⃣  View Inventory");
        System.out.println(" 4️⃣  Stock Warnings & Swap Suggestions");
        System.out.println(" 5️⃣  Log out");
        System.out.println("=========================================");
        System.out.print("Enter your choice (1-5): ");

        Scanner inp = new Scanner(System.in);
        try {
            int operation = inp.nextInt();
            switch (operation){
                case 1: addMedicine(); break;
                case 2: dropMedicine(); break;
                case 3: countMedicine(); break;
                case 4: warning(); break;
                case 5:
                    System.out.println("👋 Logged out successfully!");
                    PharmacyManager.currentPharmacy = null;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1-5.");
                    UserScreen();
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            inp.nextLine();
            UserScreen();
        }
    }
}
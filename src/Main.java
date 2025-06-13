import java.util.Map;
import java.util.Scanner;

public class Main {

    static boolean quit = false;
    private static PharmacyManager manager = new PharmacyManager();

    public static void main(String[] args) {
        clearConsole();
        System.out.println("🏥 Welcome to Pharmacy Management System!");
        System.out.println("Loading existing pharmacy data...\n");

        while(!quit) {
            StartScreen();
        }
    }

    public static void SignUp(){
        Scanner scanner = new Scanner(System.in);
        clearConsole();

        System.out.println("\n=== PHARMACY REGISTRATION ===");
        System.out.print("Enter pharmacy name (or press Enter for auto-generated name): ");
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
        System.out.println("✅ Pharmacy signed up successfully!");
        System.out.println("You are now logged in as: " + name);
        UserScreen();
    }

    public static void LogIn(){
        while(true) {
            Scanner scanner = new Scanner(System.in);
            clearConsole();

            System.out.println("\n=== PHARMACY LOGIN ===");

            // Show available pharmacies
            manager.displayAllPharmacies();

            System.out.print("Pharmacy Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean exist = manager.checkPharmacy(name, password);
            if (exist) {
                System.out.println("✅ Login successful!");
                System.out.println("Welcome back, " + name + "!");
                UserScreen();
                break;
            } else {
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
        clearConsole();
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
                System.out.println("Quantity cannot be negative!");
                addMedicine();
                return;
            }

            PharmacyManager.currentPharmacy.addMedicine(name, quantity);
            manager.saveInventory(PharmacyManager.currentPharmacy);
            System.out.println("✅ Medicine '" + name + "' added successfully!");
            System.out.println("New quantity: " + PharmacyManager.currentPharmacy.getQuantity(name));
        } catch (Exception e) {
            System.out.println("❌ Invalid quantity. Please enter a valid number.");
            scanner.nextLine(); // consume invalid input
            addMedicine();
            return;
        }

        UserScreen();
    }

    public static void dropMedicine() { //remove medicine yapıldığında quantity sıfırlanıyor
        Scanner scanner = new Scanner(System.in);
        clearConsole();
        System.out.println("\n=== REMOVE MEDICINE ===");

        // Show current inventory
        if (PharmacyManager.currentPharmacy.getInventory().isEmpty()) {
            System.out.println("No medicines in inventory!");
            UserScreen();
            return;
        }

        System.out.print("\nEnter medicine name to remove: ");
        String name = scanner.nextLine().trim();

        if (!PharmacyManager.currentPharmacy.getInventory().containsKey(name)) {
            System.out.println("❌ Medicine '" + name + "' not found in inventory!");
            UserScreen();
            return;
        }

        int currentQuantity = PharmacyManager.currentPharmacy.getQuantity(name);
        System.out.println("Current quantity of '" + name + "': " + currentQuantity);
        System.out.print("Enter quantity to remove: ");

        try {
            int quantity = scanner.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative!");
                dropMedicine();
                return;
            }

            PharmacyManager.currentPharmacy.dropMedicine(name, quantity);
            manager.saveInventory(PharmacyManager.currentPharmacy);

            int newQuantity = PharmacyManager.currentPharmacy.getQuantity(name);
            System.out.println("✅ Medicine removed successfully!");
            System.out.println("New quantity: " + newQuantity);

            if (newQuantity < manager.getWarningThreshold()) {
                System.out.println("⚠️  WARNING: '" + name + "' is now below threshold (" + manager.getWarningThreshold() + ")!");
            }

        } catch (Exception e) {
            System.out.println("❌ Invalid quantity. Please enter a valid number.");
            scanner.nextLine(); // consume invalid input
            dropMedicine();
            return;
        }

        UserScreen();
    }

    public static void countMedicine() {
        clearConsole();
        System.out.println("\n=== INVENTORY COUNT ===");

        if (PharmacyManager.currentPharmacy.getInventory().isEmpty()) {
            System.out.println("No medicines in inventory!");
        } else {
            System.out.println("Current inventory for " + PharmacyManager.currentPharmacy.getName() + ":");
            System.out.println("----------------------------------------");

            int totalMedicines = 0;
            for (Map.Entry<String, Integer> entry : PharmacyManager.currentPharmacy.getInventory().entrySet()) {
                String status = entry.getValue() < manager.getWarningThreshold() ? " ⚠️ LOW" : " ✅ OK";
                System.out.printf("%-20s : %3d%s%n", entry.getKey(), entry.getValue(), status);
                totalMedicines += entry.getValue();
            }
            System.out.println("----------------------------------------");
            System.out.println("Total medicines: " + totalMedicines);
        }

        UserScreen();
    }

    public static void warning() {
        clearConsole();
        manager.reloadAllInventories(); // Refresh data from all pharmacies
        manager.checkSwapWarning();
        UserScreen();
    }

    public static void StartScreen(){
        System.out.println("=========================================");
        System.out.println("🏥 PHARMACY MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        System.out.println(" 1️⃣  Sign Up (Create New Pharmacy)");
        System.out.println(" 2️⃣  Log In (Existing Pharmacy)");
        System.out.println(" 3️⃣  Quit");
        System.out.println("=========================================");
        System.out.print("Enter your choice (1-3): ");

        Scanner inp = new Scanner(System.in);
        try {
            int operation = inp.nextInt();
            clearConsole();
            switch (operation){
                case 1: SignUp(); break;
                case 2: LogIn(); break;
                case 3:
                    System.out.println("👋 Thank you for using Pharmacy Management System!");
                    quit = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1, 2, or 3.");
                    StartScreen();
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            inp.nextLine(); // consume invalid input
            StartScreen();
        }
    }

    public static void UserScreen(){
        clearConsole();
        System.out.println("\n=========================================");
        System.out.println("🏥 WELCOME " + PharmacyManager.currentPharmacy.getName().toUpperCase());
        System.out.println("=========================================");
        System.out.println(" 1️⃣  Add Medicine");
        System.out.println(" 2️⃣  Remove Medicine");
        System.out.println(" 3️⃣  View Inventory");
        System.out.println(" 4️⃣  Stock Warnings & Swap Suggestions");
        System.out.println(" 5️⃣  Logout");
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
        } catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            inp.nextLine(); // consume invalid input
            UserScreen();
        }
    }
    public static void clearConsole(){
        for (int i = 0; i < 64; i++) {
            System.out.println();
        }
    }
}
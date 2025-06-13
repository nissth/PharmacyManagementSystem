import java.io.*;
import java.util.*;

public class PharmacyManager {
    private List<Pharmacy> pharmacies;
    private static final String BASE_PATH = "pharmacy/";
    private static final String ACCOUNTS_FILE = "pharmacies.txt";
    public static Pharmacy currentPharmacy;
    private static final int WARNING_THRESHOLD = 5;
    public PharmacyManager() {
        pharmacies = new ArrayList<>();
        createDirectoryIfNotExists();
        loadPharmacyAccounts();
    }
    private void createDirectoryIfNotExists() {
        File dir = new File(BASE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void addPharmacy(String name, String password) {
        // Check if pharmacy already exists
        for (Pharmacy p : pharmacies) {
            if (p.getName().equals(name)) {
                System.out.println("Pharmacy with this name already exists!");
                return;
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            writer.println(name + "," + password);
            Pharmacy newPharmacy = new Pharmacy(name, password);
            pharmacies.add(newPharmacy);
            saveInventory(newPharmacy);
            currentPharmacy = newPharmacy;
            System.out.println("Pharmacy '" + name + "' created successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public boolean checkPharmacy(String name, String password) {
        for (Pharmacy p : pharmacies) {
            if (p.getName().equals(name) && p.getPassword().equals(password)) {
                currentPharmacy = p;
                loadInventory(currentPharmacy);
                return true;
            }
        }
        return false;
    }

    public void loadPharmacyAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String password = parts[1].trim();
                    Pharmacy pharmacy = new Pharmacy(name, password);
                    pharmacies.add(pharmacy);
                }
            }
        } catch (IOException e) {
            System.out.println("No pharmacy accounts found.");
        }
    }

    public void loadInventory(Pharmacy pharmacy) {
        HashMap<String, Integer> inventory = new HashMap<>();
        String filename = BASE_PATH + pharmacy.getName() + ".txt";

        File file = new File(filename);
        if (!file.exists()) {
            pharmacy.setInventory(inventory);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String medicineName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    inventory.put(medicineName, quantity);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory for " + pharmacy.getName());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing inventory data for " + pharmacy.getName());
        }

        pharmacy.setInventory(inventory);
    }

    public void saveInventory(Pharmacy pharmacy) {
        String filename = BASE_PATH + pharmacy.getName() + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Integer> entry : pharmacy.getInventory().entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory for " + pharmacy.getName());
        }
    }

    // Generate next available pharmacy name (pharmacy6, pharmacy7, etc.)
    public String generateNextPharmacyName() {
        int counter = 1;
        String baseName = "pharmacy";

        while (true) {
            String candidateName = baseName + counter;
            boolean exists = false;

            for (Pharmacy p : pharmacies) {
                if (p.getName().equals(candidateName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return candidateName;
            }
            counter++;
        }
    }

    // Check for low stock and provide swap warnings
    public void checkSwapWarning() {
        if (currentPharmacy == null) {
            System.out.println("No pharmacy logged in.");
            return;
        }

        System.out.println("\n=== STOCK WARNINGS ===");
        boolean hasWarnings = false;

        for (Map.Entry<String, Integer> entry : currentPharmacy.getInventory().entrySet()) {
            String medicineName = entry.getKey();
            int quantity = entry.getValue();

            if (quantity < WARNING_THRESHOLD) {
                hasWarnings = true;
                System.out.println("⚠️  LOW STOCK: " + medicineName + " - only " + quantity + " left!");

                // Check other pharmacies for this medicine
                List<String> availablePharmacies = findMedicineInOtherPharmacies(medicineName);

                if (!availablePharmacies.isEmpty()) {
                    System.out.println("   💡 SWAP SUGGESTION: This medicine is available in:");
                    for (String pharmacyInfo : availablePharmacies) {
                        System.out.println("      - " + pharmacyInfo);
                    }
                } else {
                    System.out.println("   ❌ This medicine is not available in other pharmacies.");
                }
                System.out.println();
            }
        }

        if (!hasWarnings) {
            System.out.println("✅ All medicines are above the warning threshold!");
        }
    }

    // Find medicine in other pharmacies
    private List<String> findMedicineInOtherPharmacies(String medicineName) {
        List<String> availablePharmacies = new ArrayList<>();

        for (Pharmacy pharmacy : pharmacies) {
            if (!pharmacy.getName().equals(currentPharmacy.getName())) {
                // Load inventory for other pharmacy
                loadInventory(pharmacy);

                int quantity = pharmacy.getQuantity(medicineName);
                if (quantity > WARNING_THRESHOLD) {
                    availablePharmacies.add(pharmacy.getName());
                }
            }
        }

        return availablePharmacies;
    }

    // Get all pharmacy names for display
    public void displayAllPharmacies() {
        System.out.println("\n=== ALL REGISTERED PHARMACIES ===");
        if (pharmacies.isEmpty()) {
            System.out.println("No pharmacies registered yet.");
        } else {
            for (int i = 0; i < pharmacies.size(); i++) {
                System.out.println((i + 1) + ". " + pharmacies.get(i).getName());
            }
        }
        System.out.println();
    }

    // Reload all inventories to get fresh data
    public void reloadAllInventories() {
        for (Pharmacy pharmacy : pharmacies) {
            loadInventory(pharmacy);
        }
    }

    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    public int getWarningThreshold() {
        return WARNING_THRESHOLD;
    }
}
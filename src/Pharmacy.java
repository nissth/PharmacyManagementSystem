import java.util.HashMap;
import java.util.Map;

public class Pharmacy {
    private String name;
    private String password;
    private HashMap<String, Integer> inventory;

    public Pharmacy(String name, String password) {
        this.name = name;
        this.password = password;
        this.inventory = new HashMap<>();
    }
    public void addMedicine(String medName, int quantity) {
        if (medName == null || medName.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be null or empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        String cleanName = medName.trim().toLowerCase();
        inventory.put(cleanName, inventory.getOrDefault(cleanName, 0) + quantity);
    }
    public void dropMedicine(String medName, int quantity) {
        if (medName == null || medName.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        String cleanName = medName.trim().toLowerCase();
        if (inventory.containsKey(cleanName)) {
            int current = inventory.get(cleanName);
            int actualDropped = Math.min(quantity, current); // Only drop what's available
            int newQuantity = current - actualDropped;

            if (newQuantity == 0) {
                inventory.remove(cleanName);
            }
            else {
                inventory.put(cleanName, newQuantity);
            }

            System.out.println("Dropped " + actualDropped + " of " + medName);
        }
        else {
            System.out.println("Warning: Medicine '" + medName + "' not found in inventory");
        }
    }
    public int getQuantity(String medName) {
        if (medName == null || medName.trim().isEmpty()) {return 0;}
        String cleanName = medName.trim().toLowerCase();
        return inventory.getOrDefault(cleanName, 0);
    }
    public void setInventory(HashMap<String, Integer> inventory) {
        this.inventory = inventory != null ? inventory : new HashMap<>();
    }
    public boolean hasMedicine(String medName) {
        if (medName == null || medName.trim().isEmpty()) {
            return false;
        }
        String cleanName = medName.trim().toLowerCase();
        return inventory.containsKey(cleanName) && inventory.get(cleanName) > 0;
    }
    public int getTotalMedicineTypes() {return inventory.size();}
    public int getTotalQuantity() {
        return inventory.values().stream().mapToInt(Integer::intValue).sum();
    }
    public boolean isInventoryEmpty() {
        return inventory.isEmpty();
    }
    public HashMap<String, Integer> getMedicinesBelowThreshold(int threshold) {
        HashMap<String, Integer> lowStockMedicines = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() < threshold) {
                lowStockMedicines.put(entry.getKey(), entry.getValue());
            }
        }
        return lowStockMedicines;
    }
    public void setMedicineQuantity(String medName, int quantity) {
        if (medName == null || medName.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be null or empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        String cleanName = medName.trim().toLowerCase();
        if (quantity == 0) {
            inventory.remove(cleanName);
        } else {
            inventory.put(cleanName, quantity);
        }
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "name='" + name + '\'' +
                ", totalMedicines=" + getTotalMedicineTypes() +
                ", totalQuantity=" + getTotalQuantity() +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Pharmacy pharmacy = (Pharmacy) obj;
        return name.equals(pharmacy.name);
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
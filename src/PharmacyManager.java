import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyManager {
    private List<Pharmacy> pharmacies;
    public PharmacyManager() {
        pharmacies = new ArrayList<>();
    }

    public void addPharmacy(String name, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("pharmacies.txt", true))) {
            writer.println(name + "," + password);
            Pharmacy newPharmacy = new Pharmacy(name, password);
            pharmacies.add(newPharmacy);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    public boolean checkPharmacy(String name, String password) { //checking log in infos
        try (BufferedReader reader = new BufferedReader(new FileReader("pharmacies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedName = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (storedName.equals(name) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }
    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }
}


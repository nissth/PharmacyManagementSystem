import java.util.LinkedList;

public class Pharmacy {
    private String name;
    private String password;
    private LinkedList medicines;
    public Pharmacy(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {return name;}
    public String getPassword() {return password;}
}

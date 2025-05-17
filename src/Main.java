import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StartScreen();
    }

    public static void SignUp(){
        Scanner scanner = new Scanner(System.in);
        PharmacyManager manager = new PharmacyManager();

        System.out.print("Enter pharmacy name: ");
        String name = scanner.nextLine();
        System.out.print("Enter pharmacy password: ");
        String password = scanner.nextLine();
        manager.addPharmacy(name, password);
        System.out.println("Pharmacy signed up.");
        SecondScreen();
    }
    public static void LogIn(){
        while(true) {
            Scanner scanner = new Scanner(System.in);
            PharmacyManager manager = new PharmacyManager();

            System.out.print("Name   : ");
            String name = scanner.nextLine();
            System.out.print("Password : ");
            String password = scanner.nextLine();
            boolean exist = manager.checkPharmacy(name, password);
            if (exist) {
                System.out.println("Pharmacy logged in.");
                SecondScreen();
                break;
            } else {
                System.out.println("Invalid Pharmacy name or password.");
                System.out.print("Do you want to try again? (Y / N): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }

    }
    public static void addMedicine(){

    }
    public static void dropMedicine(){

    }
    public static void countMedicine(){

    }
    public static void warning(){

    }

    public static void StartScreen(){
        System.out.println("---------------------------------\n");
        System.out.println(" PHARMACY MANAGEMENT SYSTEM \n");
        System.out.println(" 1 : Sign Up \n");
        System.out.println(" 2 : Log In \n");
        System.out.println("---------------------------------\n");
        System.out.print(" Enter the operation : ");
        Scanner inp = new Scanner(System.in);
        int operation = inp.nextInt();
        switch (operation){
            case 1: SignUp(); break;
            case 2: LogIn(); break;
        }
    }
    public static void SecondScreen(){
        System.out.println("---------------------------------\n");
        System.out.println(" WELCOME  " + "pharmacy.getName() \n"); //sonrasında eczane ismi yazdırılacak
        System.out.println(" 1 : Add Medicine ");
        System.out.println(" 2 : Drop Medicine ");
        System.out.println(" 3 : Medicine Quantity ");
        System.out.println(" 4 : Stock Warnings \n");
        System.out.println("---------------------------------\n");
        System.out.print("Enter the operation : ");
        Scanner inp = new Scanner(System.in);
        int operation = inp.nextInt();
        switch (operation){
            case 1: addMedicine(); break;
            case 2: dropMedicine(); break;
            case 3: countMedicine(); break;
            case 4: warning(); break;
        }
    }
}
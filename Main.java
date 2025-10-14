import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Message: ");
        String message = sc.nextLine();
        System.out.println(message);
        sc.close();
    }
}

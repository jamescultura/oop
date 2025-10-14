public class Main {
    public static void main(String[] args) {
        Account acc = new Account();
        acc.setBalance(10);
        System.out.println("Balance: " + acc.getBalance());
    }
}

class Account {
    private double balance;

    public void setBalance(double amount) {
        if (amount >= 0) {
            this.balance = amount;
        } else {
            System.out.println("Invalid");
        }
    }

    public double getBalance() {
        return this.balance;
    }
}

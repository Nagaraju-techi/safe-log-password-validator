import java.util.ArrayList;
import java.util.Scanner;

class InSufficientFundsException extends Exception {
    public InSufficientFundsException(String message) {
        super(message);
    }
}

class Account {

    private double balance;
    private String accountHolder ;
    private ArrayList<Double> transactionHistory;

    public Account(String accountHolder, double initialBalance) {
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        balance += amount;
        addTransaction(amount);
        System.out.println(" Deposit successful. Current Balance is : " + balance);
    }

    public void processTransaction(double amount)
            throws InSufficientFundsException {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        if (amount > balance) {
            throw new InSufficientFundsException("Insufficient balance for this transaction.");
        }

        balance -= amount;
        addTransaction(-amount);
        System.out.println("Withdrawal successful. Remaining Balance: " + balance);
    }

    private void addTransaction(double amount) {
        if (transactionHistory.size() == 5) {
            transactionHistory.remove(0); // remove oldest
        }
        transactionHistory.add(amount);
    }

    public void printMiniStatement() {
        System.out.println("\n Last Transactions:");
        System.out.println("the user  is :"+accountHolder);
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (double t : transactionHistory) {
            if (t > 0) {
                System.out.println("Deposit: +" + t);
            } else {
                System.out.println("Withdraw: " + t);
            }
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class FinSafeApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Initial Balance: ");
        double initialBalance = sc.nextDouble();

        Account account = new Account(name, initialBalance);

        int choice;

        do {
            System.out.println("\n===== FinSafe Menu =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Mini Statement");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            try {
                switch (choice) {

                    case 1:
                        System.out.print("Enter deposit amount: ");
                        double depAmount = sc.nextDouble();
                        account.deposit(depAmount);
                        break;

                    case 2:
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawAmount = sc.nextDouble();
                        account.processTransaction(withdrawAmount);
                        break;

                    case 3:
                        account.printMiniStatement();
                        break;

                    case 4:
                        System.out.println(" Current Balance: " + account.getBalance());
                        break;

                    case 5:
                        System.out.println("Thank you for using FinSafe");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (InSufficientFundsException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("error" + e.getMessage());
            }

        } while (choice != 5);

        sc.close();
    }
}
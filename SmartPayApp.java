import java.util.Scanner;

interface Billable {
    double calculateTotal();
}


class UtilityBill implements Billable {

    private String customerName;
    private int previousReading;
    private int currentReading;
    private int unitsConsumed;
    private double totalAmount;

    public UtilityBill(String customerName, int previousReading, int currentReading) {
        this.customerName = customerName;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
        this.unitsConsumed = currentReading - previousReading;
    }

    public double calculateTotal() {

        double amount = 0;

        if (unitsConsumed <= 100) {
            amount = unitsConsumed * 1.0;
        } 
        else if (unitsConsumed <= 300) {
            amount = (100 * 1.0) + (unitsConsumed - 100) * 2.0;
        } 
        else {
            amount = (100 * 1.0) + (200 * 2.0) + (unitsConsumed - 300) * 5.0;
        }

        totalAmount = amount;
        return totalAmount;
    }

    public void printReceipt() {
        System.out.println("\n========== SmartPay Digital Receipt ==========");
        System.out.println("Customer Name     : " + customerName);
        System.out.println("Units Consumed    : " + unitsConsumed);
        System.out.println("Total Amount ($)  : " + totalAmount);
        System.out.println("==============================================");
    }

    public boolean isValid() {
        if (previousReading > currentReading) {
            System.out.println(" Error: Previous reading cannot be greater than current reading.");
            return false;
        }
        return true;
    }
}

public class SmartPayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print("\nEnter Customer Name (or type 'Exit' to quit): ");
            String name = sc.nextLine();

            if (name.equalsIgnoreCase("Exit")) {
                System.out.println("Thank you for using SmartPay!");
                break;
            }

            try {
                System.out.print("Enter Previous Meter Reading: ");
                int prev = Integer.parseInt(sc.nextLine());

                System.out.print("Enter Current Meter Reading: ");
                int curr = Integer.parseInt(sc.nextLine());

                UtilityBill bill = new UtilityBill(name, prev, curr);

                if (!bill.isValid()) {
                    continue;
                }

                bill.calculateTotal();
                bill.printReceipt();

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter numeric values for readings.");
            }
        }

        sc.close();
    }
}
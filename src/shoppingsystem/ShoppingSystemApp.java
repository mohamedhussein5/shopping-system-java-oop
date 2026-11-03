package shoppingsystem;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * ShoppingSystemApp is the console interface. It reads the
 * user's input, follows the transaction flowchart, and catches
 * the exceptions thrown by the other classes so that the user
 * sees a friendly message.
 */
public class ShoppingSystemApp
{
    private static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args)
    {
        Inventory inventory = new Inventory();
        CustomerRegistry registry = new CustomerRegistry();
        loadSampleData(inventory, registry);

        CheckoutRegister register =
                new CheckoutRegister(inventory, registry);
        register.addDiscountPolicy(new MembershipDiscount(0.10));
        register.addDiscountPolicy(new QuantityDiscount(5, 0.05));

        String line = "==========================================";
        System.out.println(line);
        System.out.println("     WELCOME TO THE SHOPPING SYSTEM");
        System.out.println(line);

        Customer customer = identifyCustomer(register);
        if (customer == null)
        {
            System.out.println("Transaction cancelled. Goodbye.");
            return;
        }
        shop(register, inventory);
        if (checkoutAndPay(register))
        {
            System.out.println("\nUpdated stock levels:");
            inventory.displayProducts();
        }
    }

    /**
     * Creates the store's sample customers and products.
     */
    private static void loadSampleData(Inventory inventory,
                                       CustomerRegistry registry)
    {
        registry.addCustomer(new Customer("C001", "Aisha Rahman",
                CustomerType.MEMBER));
        registry.addCustomer(new Customer("C002", "Daniel Lee",
                CustomerType.REGULAR));
        registry.addCustomer(new Customer("C003", "Siti Nur",
                CustomerType.MEMBER));

        inventory.addProduct(new Product("P01", "Groceries",
                "Rice 5kg", 18.90, 20));
        inventory.addProduct(new Product("P02", "Groceries",
                "Cooking Oil 2L", 12.50, 15));
        inventory.addProduct(new Product("P03", "Beverages",
                "Milk 1L", 6.20, 10));
        inventory.addProduct(new Product("P04", "Snacks",
                "Chocolate Bar", 3.50, 30));
        inventory.addProduct(new Product("P05", "Household",
                "Detergent 3kg", 24.90, 8));
        inventory.addProduct(new Product("P06", "Stationery",
                "Notebook A5", 4.00, 3));
    }

    /**
     * Asks for a customer ID until a customer is found or the
     * user cancels.
     * @return The customer, or null if the user cancels.
     */
    private static Customer identifyCustomer(
            CheckoutRegister register)
    {
        while (true)
        {
            String id = readLine("\nEnter customer ID: ");
            try
            {
                Customer customer = register.startTransaction(id);
                System.out.println("\nCustomer   : "
                        + customer.getName());
                System.out.println("Membership : "
                        + customer.getCustomerType());
                return customer;
            }
            catch (CustomerNotFoundException e)
            {
                System.out.println("Error: " + e.getMessage());
                if (!readYesNo("Try again? (Y/N): "))
                    return null;
            }
        }
    }

    /**
     * The shopping loop: display products, add an item, view
     * the cart, optionally remove items, and repeat until the
     * customer has finished.
     */
    private static void shop(CheckoutRegister register,
                             Inventory inventory)
    {
        boolean finished = false;
        while (!finished)
        {
            inventory.displayProducts();
            addItemToCart(register);

            ShoppingCart cart = register.getCurrentCart();
            cart.viewItems();
            while (readYesNo("Remove an item? (Y/N): "))
            {
                String id =
                        readLine("Enter product ID to remove: ");
                try
                {
                    register.removeFromCart(id);
                    System.out.println("Item removed.");
                }
                catch (ProductNotFoundException e)
                {
                    System.out.println("Error: " + e.getMessage());
                }
                cart.viewItems();
            }

            if (!readYesNo("Add more items? (Y/N): "))
            {
                if (cart.isEmpty())
                    System.out.println("Your cart is empty. "
                            + "Please add at least one item.");
                else
                    finished = true;
            }
        }
    }

    /**
     * Asks for a product and quantity until one item has been
     * added. Every shopping error is caught by one catch clause
     * (polymorphic reference to the exception).
     */
    private static void addItemToCart(CheckoutRegister register)
    {
        boolean added = false;
        while (!added)
        {
            String id = readLine("Enter product ID: ");
            int quantity = readInt("Enter quantity: ");
            try
            {
                register.addToCart(id, quantity);
                System.out.println("Item added to cart.");
                added = true;
            }
            catch (ShoppingException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the bill, takes the payment and prints the
     * receipt. A failed payment may be retried.
     * @return true if the purchase was completed.
     */
    private static boolean checkoutAndPay(
            CheckoutRegister register)
    {
        register.displayBill();
        double total = register.calculateTotal();
        boolean done = false;
        while (!done)
        {
            Payment payment = choosePayment(total);
            try
            {
                Receipt receipt = register.checkout(payment);
                System.out.println("\nPayment successful.");
                receipt.printReceipt();
                saveReceipt(receipt);
                System.out.println("Transaction complete. "
                        + "Thank you for shopping with us!");
                return true;
            }
            catch (PaymentFailedException e)
            {
                System.out.println("Error: " + e.getMessage());
                if (!readYesNo("Try another payment? (Y/N): "))
                {
                    System.out.println("Transaction cancelled. No "
                            + "payment taken; stock unchanged.");
                    done = true;
                }
            }
            catch (InsufficientStockException e)
            {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Transaction cancelled. "
                        + "No payment was taken.");
                done = true;
            }
        }
        return false;
    }

    /**
     * Lets the user choose cash or card and creates the
     * matching payment object.
     * @return A CashPayment or a CardPayment, through a
     *         Payment reference.
     */
    private static Payment choosePayment(double total)
    {
        int choice = 0;
        while (choice != 1 && choice != 2)
        {
            System.out.println("\nPayment method: 1 = Cash, "
                    + "2 = Card");
            choice = readInt("Enter choice: ");
            if (choice != 1 && choice != 2)
                System.out.println("Please enter 1 or 2.");
        }

        Payment payment;
        if (choice == 1)
        {
            double tendered = readDouble("Enter amount tendered "
                    + "(RM): ");
            payment = new CashPayment(total, tendered);
        }
        else
        {
            String type = readLine("Enter card type: ");
            String number = readLine("Enter card number: ");
            payment = new CardPayment(total, type, number);
        }
        return payment;
    }

    /**
     * Saves the receipt to a text file.
     */
    private static void saveReceipt(Receipt receipt)
    {
        String fileName = "receipt_" + receipt.getReceiptNumber()
                          + ".txt";
        try
        {
            receipt.saveToFile(fileName);
            System.out.println("Receipt saved to " + fileName);
        }
        catch (IOException e)
        {
            System.out.println("Error: the receipt could not be "
                    + "saved (" + e.getMessage() + ").");
        }
    }

    // ---------------- input helper methods ----------------

    private static String readLine(String prompt)
    {
        System.out.print(prompt);
        return keyboard.nextLine().trim();
    }

    /**
     * Reads a whole number. Letters cause an
     * InputMismatchException, which is caught here.
     */
    private static int readInt(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                int value = keyboard.nextInt();
                keyboard.nextLine();
                return value;
            }
            catch (InputMismatchException e)
            {
                keyboard.nextLine();
                System.out.println("Invalid input. Please enter "
                        + "a whole number.");
            }
        }
    }

    private static double readDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                double value = keyboard.nextDouble();
                keyboard.nextLine();
                return value;
            }
            catch (InputMismatchException e)
            {
                keyboard.nextLine();
                System.out.println("Invalid input. Please enter "
                        + "a number.");
            }
        }
    }

    private static boolean readYesNo(String prompt)
    {
        while (true)
        {
            String answer = readLine(prompt).toUpperCase();
            if (answer.equals("Y"))
                return true;
            if (answer.equals("N"))
                return false;
            System.out.println("Please enter Y or N.");
        }
    }
}

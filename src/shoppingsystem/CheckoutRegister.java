package shoppingsystem;

import java.util.ArrayList;

/**
 * CheckoutRegister is the central controller of a transaction.
 * It decides the order of the steps and delegates the work to
 * the other classes.
 */
public class CheckoutRegister
{
    private Inventory inventory;
    private CustomerRegistry customerRegistry;
    private ArrayList<DiscountPolicy> discountPolicies;
    private ShoppingCart currentCart;

    public CheckoutRegister(Inventory inventory,
                            CustomerRegistry customerRegistry)
    {
        this.inventory = inventory;
        this.customerRegistry = customerRegistry;
        discountPolicies = new ArrayList<>();
        currentCart = null;
    }

    public void addDiscountPolicy(DiscountPolicy policy)
    {
        discountPolicies.add(policy);
    }

    /**
     * Finds the customer and creates a new, empty cart.
     * @exception CustomerNotFoundException When the ID is
     *            unknown.
     */
    public Customer startTransaction(String customerId)
            throws CustomerNotFoundException
    {
        Customer customer =
                customerRegistry.findCustomer(customerId);
        currentCart = new ShoppingCart(customer);
        return customer;
    }

    public ShoppingCart getCurrentCart()
    {
        return currentCart;
    }

    public void addToCart(String productId, int quantity)
            throws ProductNotFoundException,
                   InvalidQuantityException,
                   InsufficientStockException
    {
        Product product = inventory.findProduct(productId);
        currentCart.addItem(product, quantity);
    }

    public void removeFromCart(String productId)
            throws ProductNotFoundException
    {
        currentCart.removeItem(productId);
    }

    public double calculateSubtotal()
    {
        return roundToCents(currentCart.calculateSubtotal());
    }

    /**
     * Asks every discount policy for its discount and keeps
     * the largest one (polymorphism through the interface).
     * @return The best policy, or null if none applies.
     */
    public DiscountPolicy findBestDiscount()
    {
        DiscountPolicy best = null;
        double bestAmount = 0.0;
        for (DiscountPolicy policy : discountPolicies)
        {
            double amount = policy.calculateDiscount(currentCart);
            if (amount > bestAmount)
            {
                bestAmount = amount;
                best = policy;
            }
        }
        return best;
    }

    public double calculateDiscount()
    {
        DiscountPolicy best = findBestDiscount();
        if (best == null)
            return 0.0;
        return roundToCents(best.calculateDiscount(currentCart));
    }

    public double calculateTotal()
    {
        double total = calculateSubtotal() - calculateDiscount();
        return roundToCents(total);
    }

    /**
     * Displays the bill before payment.
     */
    public void displayBill()
    {
        DiscountPolicy best = findBestDiscount();
        String line = "======================================";
        System.out.println("\n" + line + "\n                BILL");
        System.out.println(line);
        System.out.printf("Subtotal : RM %8.2f%n",
                          calculateSubtotal());
        if (best == null)
            System.out.println("Discount : none");
        else
            System.out.println("Discount : "
                               + best.getDescription());
        System.out.printf("           RM %8.2f%n",
                          -calculateDiscount());
        System.out.printf("TOTAL    : RM %8.2f%n",
                          calculateTotal());
        System.out.println(line);
    }

    /**
     * Completes the purchase: checks stock, takes the payment,
     * deducts stock and creates the receipt.
     * @param payment A CashPayment or CardPayment object.
     * @return The receipt of the purchase.
     * @exception PaymentFailedException When payment fails.
     * @exception InsufficientStockException When the stock
     *            has changed since the items were added.
     */
    public Receipt checkout(Payment payment)
            throws PaymentFailedException,
                   InsufficientStockException
    {
        ArrayList<CartItem> items = currentCart.getItems();
        inventory.checkStock(items);    // before taking money
        payment.pay();                  // dynamic binding
        inventory.deductStock(items);   // only after payment

        DiscountPolicy best = findBestDiscount();
        String description = "None";
        if (best != null)
            description = best.getDescription();

        Receipt receipt = new Receipt(currentCart.getCustomer(),
                items, calculateSubtotal(), calculateDiscount(),
                description, payment);
        currentCart.clear();
        return receipt;
    }

    /**
     * Rounds a money value to two decimal places so that
     * amounts such as 89.99999999 become 90.00.
     */
    private static double roundToCents(double value)
    {
        return Math.round(value * 100) / 100.0;
    }
}

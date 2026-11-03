package shoppingsystem;

import java.util.ArrayList;

/**
 * ShoppingCart holds the items selected by one customer.
 */
public class ShoppingCart
{
    private Customer customer;
    private ArrayList<CartItem> items;

    /**
     * Constructor: a cart always belongs to one customer.
     * @param customer The owner of the cart.
     */
    public ShoppingCart(Customer customer)
    {
        this.customer = customer;
        items = new ArrayList<>();
    }

    /**
     * Adds a product to the cart after checking the quantity
     * and the stock (including units already in the cart).
     * @param product  The product to add.
     * @param quantity The number of units.
     * @exception InvalidQuantityException   When quantity < 1.
     * @exception InsufficientStockException When the stock is
     *            too low.
     */
    public void addItem(Product product, int quantity)
            throws InvalidQuantityException,
                   InsufficientStockException
    {
        if (quantity < 1)
            throw new InvalidQuantityException(quantity);

        CartItem existing = findItem(product.getProductId());
        int alreadyInCart = 0;
        if (existing != null)
            alreadyInCart = existing.getQuantity();

        if (!product.isAvailable(alreadyInCart + quantity))
            throw new InsufficientStockException(
                    product.getDescription(),
                    product.getQuantityInStock() - alreadyInCart);

        if (existing == null)
            items.add(new CartItem(product, quantity));
        else
            existing.addQuantity(quantity);
    }

    /**
     * Removes a product from the cart.
     * @param productId The ID of the product to remove.
     * @exception ProductNotFoundException When the product is
     *            not in the cart.
     */
    public void removeItem(String productId)
            throws ProductNotFoundException
    {
        CartItem item = findItem(productId);
        if (item == null)
            throw new ProductNotFoundException(productId);
        items.remove(item);
    }

    /**
     * Displays the items in the cart and the subtotal.
     */
    public void viewItems()
    {
        System.out.println("\nCart of " + customer.getName() + ":");
        if (items.isEmpty())
        {
            System.out.println("  (the cart is empty)");
            return;
        }
        System.out.printf("%-5s %-16s %4s %9s %10s%n", "ID",
                "Description", "Qty", "Price", "Line Total");
        for (CartItem item : items)
            System.out.println(item);
        System.out.printf("Subtotal: RM %.2f%n",
                          calculateSubtotal());
    }

    /**
     * @return A copy of the item list, so that callers cannot
     *         change the cart directly.
     */
    public ArrayList<CartItem> getItems()
    {
        return new ArrayList<>(items);
    }

    public Customer getCustomer()
    {
        return customer;
    }

    /**
     * @return The total number of units in the cart.
     */
    public int getTotalQuantity()
    {
        int total = 0;
        for (CartItem item : items)
            total += item.getQuantity();
        return total;
    }

    /**
     * @return The sum of all line totals.
     */
    public double calculateSubtotal()
    {
        double subtotal = 0.0;
        for (CartItem item : items)
            subtotal += item.getLineTotal();
        return subtotal;
    }

    public boolean isEmpty()
    {
        return items.isEmpty();
    }

    public void clear()
    {
        items.clear();
    }

    /**
     * Searches the cart for a product (not case-sensitive).
     * @param productId The product ID.
     * @return The cart line, or null if it is not in the cart.
     */
    private CartItem findItem(String productId)
    {
        for (CartItem item : items)
        {
            String id = item.getProduct().getProductId();
            if (id.equalsIgnoreCase(productId))
                return item;
        }
        return null;
    }
}

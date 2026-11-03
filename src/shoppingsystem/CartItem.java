package shoppingsystem;

/**
 * CartItem is one line of a shopping cart: a product and the
 * quantity the customer is buying.
 */
public class CartItem
{
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct()
    {
        return product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Increases the quantity when the same product is added
     * to the cart again.
     * @param amount The number of extra units.
     */
    public void addQuantity(int amount)
    {
        quantity += amount;
    }

    /**
     * @return The unit price multiplied by the quantity.
     */
    public double getLineTotal()
    {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString()
    {
        return String.format("%-5s %-16s %4d %9.2f %10.2f",
                product.getProductId(), product.getDescription(),
                quantity, product.getPrice(), getLineTotal());
    }
}

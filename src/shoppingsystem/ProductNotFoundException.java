package shoppingsystem;

/**
 * Thrown when a product ID does not exist in the inventory
 * or is not in the shopping cart.
 */
public class ProductNotFoundException extends ShoppingException
{
    public ProductNotFoundException(String productId)
    {
        super("Product ID '" + productId + "' was not found.");
    }
}

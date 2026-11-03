package shoppingsystem;

/**
 * Thrown when the quantity entered is less than 1.
 */
public class InvalidQuantityException extends ShoppingException
{
    public InvalidQuantityException(int quantity)
    {
        super("Invalid quantity: " + quantity
              + ". Quantity must be at least 1.");
    }
}

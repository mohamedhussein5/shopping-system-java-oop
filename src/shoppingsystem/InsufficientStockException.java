package shoppingsystem;

/**
 * Thrown when there are not enough units in stock.
 */
public class InsufficientStockException extends ShoppingException
{
    public InsufficientStockException(String productName,
                                      int available)
    {
        super("Not enough stock for '" + productName
              + "'. Only " + available + " more can be added.");
    }
}

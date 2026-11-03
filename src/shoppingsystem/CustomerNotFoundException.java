package shoppingsystem;

/**
 * Thrown when no customer has the entered ID.
 */
public class CustomerNotFoundException extends ShoppingException
{
    public CustomerNotFoundException(String customerId)
    {
        super("Customer ID '" + customerId + "' was not found.");
    }
}

package shoppingsystem;

/**
 * Thrown when a payment cannot be completed.
 */
public class PaymentFailedException extends ShoppingException
{
    public PaymentFailedException(String reason)
    {
        super("Payment failed: " + reason);
    }
}

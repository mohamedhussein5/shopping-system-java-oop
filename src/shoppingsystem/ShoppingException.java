package shoppingsystem;

/**
 * ShoppingException is the superclass of every error in the
 * shopping system, so that one catch clause can handle all of
 * them.
 */
public class ShoppingException extends Exception
{
    public ShoppingException(String message)
    {
        super(message);
    }
}

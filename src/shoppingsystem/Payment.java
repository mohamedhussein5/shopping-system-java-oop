package shoppingsystem;

/**
 * Payment is the abstract superclass of every payment method.
 * It holds the data and behaviour shared by all payments.
 */
public abstract class Payment
{
    private double amount;
    private boolean confirmed;

    /**
     * @param amount The amount to be paid.
     */
    public Payment(double amount)
    {
        this.amount = amount;
        confirmed = false;
    }

    public double getAmount()
    {
        return amount;
    }

    public boolean isConfirmed()
    {
        return confirmed;
    }

    /**
     * Only subclasses may confirm a payment, and only after
     * it has been processed successfully.
     */
    protected void markConfirmed()
    {
        confirmed = true;
    }

    /**
     * Processes the payment. Each subclass must provide its
     * own version.
     * @exception PaymentFailedException When the payment
     *            cannot be completed.
     */
    public abstract void pay() throws PaymentFailedException;

    /**
     * @return The type of payment, e.g. "Cash".
     */
    public abstract String getPaymentType();

    /**
     * @return A description of the payment for the receipt.
     */
    public String getPaymentDetails()
    {
        return getPaymentType() + " payment of RM "
               + String.format("%.2f", amount);
    }
}

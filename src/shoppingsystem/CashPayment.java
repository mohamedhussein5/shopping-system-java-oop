package shoppingsystem;

/**
 * CashPayment checks the amount handed over and calculates
 * the change.
 */
public class CashPayment extends Payment
{
    private double amountTendered;
    private double change;

    /**
     * @param amount         The amount due.
     * @param amountTendered The cash handed over.
     */
    public CashPayment(double amount, double amountTendered)
    {
        super(amount);
        this.amountTendered = amountTendered;
        change = 0.0;
    }

    @Override
    public void pay() throws PaymentFailedException
    {
        if (amountTendered < getAmount())
            throw new PaymentFailedException(String.format(
                    "RM %.2f is not enough; RM %.2f is due.",
                    amountTendered, getAmount()));
        change = amountTendered - getAmount();
        markConfirmed();
    }

    public double getChange()
    {
        return change;
    }

    @Override
    public String getPaymentType()
    {
        return "Cash";
    }

    @Override
    public String getPaymentDetails()
    {
        return super.getPaymentDetails() + String.format(
                " (tendered RM %.2f, change RM %.2f)",
                amountTendered, change);
    }
}

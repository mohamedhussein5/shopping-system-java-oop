package shoppingsystem;

/**
 * QuantityDiscount gives a percentage discount when the cart
 * holds at least a minimum number of units.
 */
public class QuantityDiscount implements DiscountPolicy
{
    private int minimumItems;
    private double rate;

    /**
     * @param minimumItems The number of units needed.
     * @param rate         The discount rate, e.g. 0.05 for 5%.
     */
    public QuantityDiscount(int minimumItems, double rate)
    {
        this.minimumItems = minimumItems;
        this.rate = rate;
    }

    @Override
    public double calculateDiscount(ShoppingCart cart)
    {
        if (cart.getTotalQuantity() >= minimumItems)
            return cart.calculateSubtotal() * rate;
        return 0.0;
    }

    @Override
    public String getDescription()
    {
        return "Quantity discount (" + (int) (rate * 100)
               + "% for " + minimumItems + "+ items)";
    }
}

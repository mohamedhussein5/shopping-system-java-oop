package shoppingsystem;

/**
 * MembershipDiscount gives members a percentage discount.
 */
public class MembershipDiscount implements DiscountPolicy
{
    private double rate;

    /**
     * @param rate The discount rate, e.g. 0.10 for 10%.
     */
    public MembershipDiscount(double rate)
    {
        this.rate = rate;
    }

    @Override
    public double calculateDiscount(ShoppingCart cart)
    {
        if (cart.getCustomer().isMember())
            return cart.calculateSubtotal() * rate;
        return 0.0;
    }

    @Override
    public String getDescription()
    {
        return "Membership discount (" + (int) (rate * 100) + "%)";
    }
}

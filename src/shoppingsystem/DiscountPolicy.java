package shoppingsystem;

/**
 * DiscountPolicy is the contract that every discount rule
 * must follow. New rules can be added without changing the
 * checkout logic.
 */
public interface DiscountPolicy
{
    /**
     * @param cart The cart to examine.
     * @return The discount amount (0 if the rule does not apply).
     */
    double calculateDiscount(ShoppingCart cart);

    /**
     * @return A description shown on the bill and receipt.
     */
    String getDescription();
}

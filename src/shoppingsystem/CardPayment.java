package shoppingsystem;

/**
 * CardPayment simulates a card payment. Only the last four
 * digits of the card number are kept.
 */
public class CardPayment extends Payment
{
    private String cardType;
    private String lastFourDigits;
    private boolean validNumber;

    /**
     * @param amount     The amount due.
     * @param cardType   The card type, e.g. "Visa".
     * @param cardNumber The full card number (not stored).
     */
    public CardPayment(double amount, String cardType,
                       String cardNumber)
    {
        super(amount);
        this.cardType = cardType;
        String digits = cardNumber.replace(" ", "");
        validNumber = digits.matches("\\d{16}");
        if (digits.length() >= 4)
            lastFourDigits = digits.substring(digits.length() - 4);
        else
            lastFourDigits = "????";
    }

    @Override
    public void pay() throws PaymentFailedException
    {
        if (!validNumber)
            throw new PaymentFailedException(
                    "the card number must have 16 digits.");
        markConfirmed();
    }

    @Override
    public String getPaymentType()
    {
        return "Card";
    }

    @Override
    public String getPaymentDetails()
    {
        return super.getPaymentDetails() + " (" + cardType
               + " ending " + lastFourDigits + ")";
    }
}

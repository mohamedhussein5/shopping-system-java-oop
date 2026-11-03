package shoppingsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Receipt is the permanent record of a completed purchase.
 */
public class Receipt
{
    private static int nextReceiptNumber = 1001;

    private int receiptNumber;
    private Customer customer;
    private ArrayList<CartItem> items;
    private double subtotal;
    private double discountAmount;
    private String discountDescription;
    private Payment payment;

    /**
     * The receipt keeps its own copy of the item list because
     * the cart is cleared after checkout.
     */
    public Receipt(Customer customer, ArrayList<CartItem> items,
                   double subtotal, double discountAmount,
                   String discountDescription, Payment payment)
    {
        receiptNumber = nextReceiptNumber;
        nextReceiptNumber++;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.discountDescription = discountDescription;
        this.payment = payment;
    }

    public int getReceiptNumber()
    {
        return receiptNumber;
    }

    public double getFinalAmount()
    {
        return subtotal - discountAmount;
    }

    public void printReceipt()
    {
        System.out.println(this);
    }

    /**
     * Writes the receipt to a text file (Chapter 6).
     * @param fileName The name of the file.
     * @exception IOException When the file cannot be written.
     */
    public void saveToFile(String fileName) throws IOException
    {
        PrintWriter outputFile = new PrintWriter(fileName);
        outputFile.print(toString());
        outputFile.close();
    }

    @Override
    public String toString()
    {
        String line = "========================"
                      + "========================";
        String dash = "------------------------"
                      + "------------------------";
        String text = "\n" + line + "\n"
                + "            SHOPPING SYSTEM RECEIPT\n"
                + line + "\n"
                + "Receipt No : " + receiptNumber + "\n"
                + "Customer   : " + customer + "\n"
                + dash + "\n"
                + String.format("%-5s %-16s %4s %9s %10s%n",
                        "ID", "Description", "Qty", "Price",
                        "Line Total");
        for (CartItem item : items)
            text += item + "\n";
        text += dash + "\n"
                + String.format("Subtotal   : RM %8.2f%n", subtotal)
                + "Discount   : " + discountDescription + "\n"
                + String.format("             RM %8.2f%n",
                                -discountAmount)
                + String.format("TOTAL PAID : RM %8.2f%n",
                                getFinalAmount())
                + "Payment    : " + payment.getPaymentType() + "\n"
                + "Details    : " + payment.getPaymentDetails()
                + "\n" + line;
        return text;
    }
}

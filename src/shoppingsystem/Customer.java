package shoppingsystem;

/**
 * Customer stores the details of one customer of the store.
 */
public class Customer
{
    private String customerId;
    private String name;
    private CustomerType customerType;

    /**
     * Constructor
     * @param customerId   The customer's unique ID.
     * @param name         The customer's name.
     * @param customerType REGULAR or MEMBER.
     */
    public Customer(String customerId, String name,
                    CustomerType customerType)
    {
        this.customerId = customerId;
        this.name = name;
        this.customerType = customerType;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    public CustomerType getCustomerType()
    {
        return customerType;
    }

    /**
     * @return true if the customer is a member.
     */
    public boolean isMember()
    {
        return customerType == CustomerType.MEMBER;
    }

    @Override
    public String toString()
    {
        return customerId + " - " + name
               + " (" + customerType + ")";
    }
}

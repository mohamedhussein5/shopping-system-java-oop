package shoppingsystem;

import java.util.ArrayList;

/**
 * CustomerRegistry stores all customers and finds one by ID.
 */
public class CustomerRegistry
{
    private ArrayList<Customer> customers;

    public CustomerRegistry()
    {
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer)
    {
        customers.add(customer);
    }

    /**
     * Finds a customer by ID (not case-sensitive).
     * @param customerId The ID to search for.
     * @return The matching customer.
     * @exception CustomerNotFoundException When no customer
     *            has the given ID.
     */
    public Customer findCustomer(String customerId)
            throws CustomerNotFoundException
    {
        for (Customer customer : customers)
        {
            String id = customer.getCustomerId();
            if (id.equalsIgnoreCase(customerId))
                return customer;
        }
        throw new CustomerNotFoundException(customerId);
    }
}

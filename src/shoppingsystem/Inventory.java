package shoppingsystem;

import java.util.ArrayList;

/**
 * Inventory stores all products sold by the store.
 */
public class Inventory
{
    private ArrayList<Product> products;

    public Inventory()
    {
        products = new ArrayList<>();
    }

    public void addProduct(Product product)
    {
        products.add(product);
    }

    /**
     * Finds a product by ID (not case-sensitive).
     * @param productId The ID to search for.
     * @return The matching product.
     * @exception ProductNotFoundException When no product has
     *            the given ID.
     */
    public Product findProduct(String productId)
            throws ProductNotFoundException
    {
        for (Product product : products)
        {
            String id = product.getProductId();
            if (id.equalsIgnoreCase(productId))
                return product;
        }
        throw new ProductNotFoundException(productId);
    }

    /**
     * Displays the menu of products and prices.
     */
    public void displayProducts()
    {
        String line = "-------------------------"
                      + "--------------------------";
        System.out.println("\n" + line);
        System.out.println("                    PRODUCTS");
        System.out.println(line);
        System.out.printf("%-5s %-11s %-16s %9s %6s%n", "ID",
                "Category", "Description", "Price(RM)", "Stock");
        for (Product product : products)
            System.out.println(product);
        System.out.println(line);
    }

    /**
     * Checks that every item can still be supplied.
     * Called before payment so that a customer is never
     * charged for stock that is not available.
     * @param items The items to check.
     * @exception InsufficientStockException When an item
     *            exceeds the stock.
     */
    public void checkStock(ArrayList<CartItem> items)
            throws InsufficientStockException
    {
        for (CartItem item : items)
        {
            Product product = item.getProduct();
            if (!product.isAvailable(item.getQuantity()))
                throw new InsufficientStockException(
                        product.getDescription(),
                        product.getQuantityInStock());
        }
    }

    /**
     * Deducts the stock for a completed purchase.
     * @param items The purchased items.
     * @exception InsufficientStockException When an item
     *            exceeds the stock.
     */
    public void deductStock(ArrayList<CartItem> items)
            throws InsufficientStockException
    {
        for (CartItem item : items)
            item.getProduct().reduceStock(item.getQuantity());
    }
}

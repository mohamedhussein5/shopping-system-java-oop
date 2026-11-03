package shoppingsystem;

/**
 * Product represents one item sold by the store.
 */
public class Product
{
    private String productId;
    private String category;
    private String description;
    private double price;
    private int quantityInStock;

    /**
     * Constructor
     * @param productId       Short code used to select the product.
     * @param category        The product category.
     * @param description     The product description.
     * @param price           The unit price.
     * @param quantityInStock The number of units in stock.
     */
    public Product(String productId, String category,
                   String description, double price,
                   int quantityInStock)
    {
        this.productId = productId;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getCategory()
    {
        return category;
    }

    public String getDescription()
    {
        return description;
    }

    public double getPrice()
    {
        return price;
    }

    public int getQuantityInStock()
    {
        return quantityInStock;
    }

    /**
     * @param quantity The number of units wanted.
     * @return true if enough units are in stock.
     */
    public boolean isAvailable(int quantity)
    {
        return quantity <= quantityInStock;
    }

    /**
     * Lowers the stock after a successful purchase.
     * @param quantity The number of units sold.
     * @exception InsufficientStockException When there are not
     *            enough units in stock.
     */
    public void reduceStock(int quantity)
            throws InsufficientStockException
    {
        if (!isAvailable(quantity))
            throw new InsufficientStockException(description,
                                                 quantityInStock);
        quantityInStock -= quantity;
    }

    @Override
    public String toString()
    {
        return String.format("%-5s %-11s %-16s %9.2f %6d",
                productId, category, description, price,
                quantityInStock);
    }
}

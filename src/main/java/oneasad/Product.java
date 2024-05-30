package oneasad;

public class Product {

    private int prod_id;
    private String prod_name;
    private double prod_price;

    public Product(int id, String name, double price) {
        this.prod_id = id;
        this.prod_name = name;
        this.prod_price = price;
    }

    // <editor-fold desc="Getter and setter methods">
    public int getId() {
        return prod_id;
    }

    public void setId(int id) {
        this.prod_id = id;
    }

    public String getName() {
        return prod_name;
    }

    public void setName(String name) {
        this.prod_name = name;
    }

    public double getPrice() {
        return prod_price;
    }

    public void setPrice(double price) {
        this.prod_price = price;
    }
    // </editor-fold>
}

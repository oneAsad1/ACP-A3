package oneasad;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    int createProduct(Product product) throws SQLException;
    Product getProductById(int id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    int updateProduct(Product product) throws SQLException;
    int deleteProduct(int id) throws SQLException;
}

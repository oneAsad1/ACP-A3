package oneasad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO{
    Connection conn;
    Statement st;
    ResultSet resultSet;

    ProductDAOImpl() {
        String url = "jdbc:mysql://localhost:3306/acp_a3";
        String user = "root";
        String pass = "ItisHard@1234";
        try {
            conn = DriverManager.getConnection(url, user, pass);
            st = conn.createStatement();
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public int createProduct(Product product) throws SQLException {
        PreparedStatement pStat = conn.prepareStatement("INSERT INTO products (prod_name, prod_price) VALUES (?, ?)");
        pStat.setString(1, product.getName());
        pStat.setDouble(2, product.getPrice());
        return pStat.executeUpdate();
    }

    @Override
    public Product getProductById(int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM products WHERE prod_id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int prod_id = resultSet.getInt("prod_id");
            String prod_name = resultSet.getString("prod_name");
            double prod_price = resultSet.getDouble("prod_price");
            return new Product(prod_id, prod_name, prod_price);
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        resultSet = st.executeQuery("SELECT * FROM products");
        ResultSetMetaData rsmd = resultSet.getMetaData();

        int columnCount = rsmd.getColumnCount();

        Object[] rowData = new Object[columnCount];
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            products.add(new Product(Integer.parseInt(rowData[0].toString()), rowData[1].toString(), Double.parseDouble(rowData[2].toString())));
        }
        return products;
    }

    @Override
    public int updateProduct(Product product) throws SQLException {
        PreparedStatement pStat = conn.prepareStatement("UPDATE products SET prod_name = ?, prod_price = ? WHERE prod_id = ?");
        pStat.setString(1, product.getName());
        pStat.setDouble(2, product.getPrice());
        pStat.setInt(3, product.getId());
        return pStat.executeUpdate();
    }

    @Override
    public int deleteProduct(int id) throws SQLException {
        PreparedStatement pStat = conn.prepareStatement("DELETE FROM products WHERE prod_id = ?");
        pStat.setInt(1, id);
        return pStat.executeUpdate();
    }
}

package oneasad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class InventoryMS implements ActionListener {

    // <editor-fold desc="Global Declaration">
    JLabel idLabel, nameLabel, priceLabel, desLabel;
    JTextField idField, nameField, priceField, desField;
    JButton submitButton;
    JComboBox<String> opsList;
    JFrame MainFrame, SecondFrame;
    JTable dataTable;
    ProductDAOImpl Prod_DAO;
    // </editor-fold>

    public InventoryMS(){
        createMainPage();
        Prod_DAO = new ProductDAOImpl();
    }

    // <editor-fold desc="Creating Main page">
    void createMainPage() {
        idLabel = new JLabel("Product ID");
        nameLabel = new JLabel("Product Name");
        priceLabel = new JLabel("Product Price");
        desLabel = new JLabel("Operation");
        idField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        desField = new JTextField("Select Ops");
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        String[] desOptions = {"Create", "Read", "Update", "Delete"};
        opsList = new JComboBox<>(desOptions);
        opsList.setBounds(50, 50, 90, 20);


        JPanel panel = new JPanel();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(grid);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(idField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(priceField, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(desLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(opsList, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        MainFrame = new JFrame("Inventory Management");
        MainFrame.add(panel, BorderLayout.CENTER);
        MainFrame.setVisible(true);
        MainFrame.setSize(300, 300);
        MainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    // </editor-fold>

    // <editor-fold desc="Creating second page">
    void createSecondPage(DefaultTableModel model) {
        dataTable = new JTable(model);
        SecondFrame = new JFrame("View Data");
        SecondFrame.add(new JScrollPane(dataTable));
        SecondFrame.setVisible(true);
        SecondFrame.setSize(300, 300);
        SecondFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    // </editor-fold>

    @Override
    public void actionPerformed(ActionEvent e) {

        // <editor-fold desc="Getting Data from text fields">
        String operation = opsList.getItemAt(opsList.getSelectedIndex());
        String name = "";
        int id_num = 0 , price_num = 0;

        if(!idField.getText().isEmpty())
            id_num = Integer.parseInt(idField.getText());
        if(!nameField.getText().isEmpty())
            name = nameField.getText();
        if(!priceField.getText().isEmpty())
            price_num = Integer.parseInt(priceField.getText());
        int result;

        // </editor-fold>

        // <editor-fold desc="Create Mechanism">
        if (operation.equals("Create") && !name.isEmpty() && !priceField.getText().isEmpty()) {
            try {
                result = Prod_DAO.createProduct(new Product(id_num, nameField.getText(), price_num));
                if (result>0)
                    JOptionPane.showMessageDialog(MainFrame, result + " Product Added Successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        // </editor-fold>

        // <editor-fold desc="Read Mechanism">
        else if (operation.equals("Read") && idField.getText().isEmpty()) {
            try {
                List<Product> productsList = Prod_DAO.getAllProducts();
                String[] columnNames = {"ID", "Name", "Price"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (Product product : productsList) {
                    Object[] rowData = new Object[columnNames.length];
                    rowData[0] = product.getId();
                    rowData[1] = product.getName();
                    rowData[2] = product.getPrice();
                    model.addRow(rowData);
                }
                createSecondPage(model);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        // </editor-fold>

        // <editor-fold desc="Reading a single object">
        else if (operation.equals("Read") && !idField.getText().isEmpty()) {
            try {
                Product prod = Prod_DAO.getProductById(id_num);
                if(prod!= null){
                    String prod_details = "Product [id: " + prod.getId() + ", name: " + prod.getName()
                        + ", price: " + prod.getPrice() + "]";
                    JOptionPane.showMessageDialog(null, prod_details, "Product Details", JOptionPane.PLAIN_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(MainFrame, "Product Not Found", "Warning", JOptionPane.WARNING_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        // </editor-fold>

        // <editor-fold desc="Update Mechanism">
        else if (operation.equals("Update") && !idField.getText().isEmpty() && !name.isEmpty() && !priceField.getText().isEmpty()) {
            try {
                result = Prod_DAO.updateProduct(new Product(id_num, name, price_num));
                if (result>0)
                    JOptionPane.showMessageDialog(MainFrame, result + " Record updated Successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        // </editor-fold>

        // <editor-fold desc="Delete Mechanism">
        else if (operation.equals("Delete") && !idField.getText().isEmpty()) {
            try {
                result = Prod_DAO.deleteProduct(id_num);
                if (result>0)
                    JOptionPane.showMessageDialog(MainFrame, "Number of Products removed: " + result);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        // </editor-fold>

        else
            JOptionPane.showMessageDialog(MainFrame, "Error in credentials", "Warning", JOptionPane.WARNING_MESSAGE);
    }//end of actionPerformed()
}

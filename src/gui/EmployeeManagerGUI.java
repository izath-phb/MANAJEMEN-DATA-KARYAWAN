package gui;

import model.Employee;
import service.MongoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class EmployeeManagerGUI extends JFrame {

    private JTextField nameField;
    private JTextField ageField;
    private JTextField deptField;
    private JTable employeeTable;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private JComboBox<String> languageSelector;

    // Label & Button yang bisa diganti bahasanya
    private JLabel nameLabel, ageLabel, deptLabel, languageLabel;
    private JButton saveBtn, viewBtn, deleteBtn;

    public EmployeeManagerGUI(String languageCode) {
        setLocaleAndBundle(languageCode);
        initUI();
    }

    private void setLocaleAndBundle(String languageCode) {
        currentLocale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("resources.messages", new Locale("id"));
    }

    private void initUI() {
        setTitle(bundle.getString("title"));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        
        nameLabel = new JLabel(bundle.getString("name"));
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        inputPanel.add(nameField);

        ageLabel = new JLabel(bundle.getString("age"));
        inputPanel.add(ageLabel);
        ageField = new JTextField();
        inputPanel.add(ageField);

        deptLabel = new JLabel(bundle.getString("department"));
        inputPanel.add(deptLabel);
        deptField = new JTextField();
        inputPanel.add(deptField);

        languageLabel = new JLabel(bundle.getString("language"));
        inputPanel.add(languageLabel);
        languageSelector = new JComboBox<>(new String[]{"English", "Indonesia"});
        languageSelector.addActionListener(e -> switchLanguage());
        inputPanel.add(languageSelector);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton(bundle.getString("save"));
        viewBtn = new JButton(bundle.getString("viewAll"));
        deleteBtn = new JButton(bundle.getString("deleteAll"));

        saveBtn.addActionListener(this::saveEmployee);
        viewBtn.addActionListener(this::viewEmployees);
        deleteBtn.addActionListener(this::deleteAllEmployees);

        buttonPanel.add(saveBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.CENTER);

        employeeTable = new JTable();
        add(new JScrollPane(employeeTable), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void switchLanguage() {
        String selected = (String) languageSelector.getSelectedItem();
        String langCode = selected.equals("Indonesia") ? "id" : "en";
        setLocaleAndBundle(langCode);

        // Perbarui semua teks di UI
        setTitle(bundle.getString("title"));
        nameLabel.setText(bundle.getString("name"));
        ageLabel.setText(bundle.getString("age"));
        deptLabel.setText(bundle.getString("department"));
        languageLabel.setText(bundle.getString("language"));

        saveBtn.setText(bundle.getString("save"));
        viewBtn.setText(bundle.getString("viewAll"));
        deleteBtn.setText(bundle.getString("deleteAll"));

        // Refresh data tabel kalau ada
        viewEmployees(null);
    }

    private void saveEmployee(ActionEvent e) {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String dept = deptField.getText();

        Employee emp = new Employee(name, age, dept);
        MongoService.saveEmployee(emp);

        JOptionPane.showMessageDialog(this, bundle.getString("dataSaved"));
    }

    private void viewEmployees(ActionEvent e) {
        List<Employee> employees = MongoService.getAllEmployees();
        String[] columnNames = {
            bundle.getString("name"),
            bundle.getString("age"),
            bundle.getString("department")
        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Employee emp : employees) {
            Object[] row = {emp.getName(), emp.getAge(), emp.getDepartment()};
            model.addRow(row);
        }

        employeeTable.setModel(model);
    }

    private void deleteAllEmployees(ActionEvent e) {
        MongoService.deleteAllEmployees();
        viewEmployees(null);
    }

    public static void main(String[] args) {
    ResourceBundle testBundle = ResourceBundle.getBundle("resources.messages", new Locale("id"));
    SwingUtilities.invokeLater(() -> new EmployeeManagerGUI("en"));
}

    
    
}

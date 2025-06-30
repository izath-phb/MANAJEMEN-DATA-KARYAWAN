package gui;

import model.Employee;
import service.MongoService;
import util.LangUtil; // Import LangUtil

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;
// ResourceBundle tidak lagi diperlukan di sini
// import java.util.ResourceBundle;

public class EmployeeManagerGUI extends JFrame {

    private JTextField nameField;
    private JTextField ageField;
    private JTextField deptField;
    private JTable employeeTable;
    private JComboBox<String> languageSelector;

    private JLabel nameLabel, ageLabel, deptLabel, languageLabel;
    private JButton saveBtn, viewBtn, deleteBtn;

    // Definisikan base name untuk bundle employee
    private final String BUNDLE_BASENAME = "messages";

    public EmployeeManagerGUI(String languageCode) {
        // Atur bundle yang benar saat inisialisasi
        LangUtil.setBundle(BUNDLE_BASENAME, new Locale(languageCode));
        initUI();
        
        // Atur pilihan combo box sesuai bahasa yang masuk
        if ("id".equals(languageCode)) {
            languageSelector.setSelectedItem("Indonesia");
        } else {
            languageSelector.setSelectedItem("English");
        }
    }

    private void initUI() {
        // Semua panggilan ke bundle.getString() diganti dengan LangUtil.get()
        setTitle(LangUtil.get("title"));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameLabel = new JLabel(LangUtil.get("name"));
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        inputPanel.add(nameField);

        ageLabel = new JLabel(LangUtil.get("age"));
        inputPanel.add(ageLabel);
        ageField = new JTextField();
        inputPanel.add(ageField);

        deptLabel = new JLabel(LangUtil.get("department"));
        inputPanel.add(deptLabel);
        deptField = new JTextField();
        inputPanel.add(deptField);

        languageLabel = new JLabel(LangUtil.get("language"));
        inputPanel.add(languageLabel);
        languageSelector = new JComboBox<>(new String[]{"English", "Indonesia"});
        languageSelector.addActionListener(e -> switchLanguage());
        inputPanel.add(languageSelector);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton(LangUtil.get("save"));
        viewBtn = new JButton(LangUtil.get("viewAll"));
        deleteBtn = new JButton(LangUtil.get("deleteAll"));

        saveBtn.addActionListener(this::saveEmployee);
        viewBtn.addActionListener(this::viewEmployees);
        deleteBtn.addActionListener(this::deleteAllEmployees);

        buttonPanel.add(saveBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.CENTER);

        employeeTable = new JTable();
        add(new JScrollPane(employeeTable), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void switchLanguage() {
        String selected = (String) languageSelector.getSelectedItem();
        String langCode = selected.equals("Indonesia") ? "id" : "en";
        
        // Atur bundle yang benar menggunakan LangUtil
        LangUtil.setBundle(BUNDLE_BASENAME, new Locale(langCode));

        // Panggil metode untuk memuat ulang teks UI
        reloadText();
    }
    
    /**
     * Metode baru untuk memuat ulang semua teks di UI.
     */
    private void reloadText() {
        setTitle(LangUtil.get("title"));
        nameLabel.setText(LangUtil.get("name"));
        ageLabel.setText(LangUtil.get("age"));
        deptLabel.setText(LangUtil.get("department"));
        languageLabel.setText(LangUtil.get("language"));

        saveBtn.setText(LangUtil.get("save"));
        viewBtn.setText(LangUtil.get("viewAll"));
        deleteBtn.setText(LangUtil.get("deleteAll"));

        // Refresh data tabel untuk memperbarui header kolom
        viewEmployees(null);
        
        revalidate();
        repaint();
    }

    private void saveEmployee(ActionEvent e) {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String dept = deptField.getText();

        if (name.isEmpty() || ageText.isEmpty() || dept.isEmpty()) {
            // Sebaiknya tambahkan pesan error dari file properti juga
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
            return;
        }
        
        try {
            int age = Integer.parseInt(ageText);
            Employee emp = new Employee(name, age, dept);
            MongoService.saveEmployee(emp);
            JOptionPane.showMessageDialog(this, LangUtil.get("dataSaved"));
            // Kosongkan field setelah menyimpan
            nameField.setText("");
            ageField.setText("");
            deptField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.");
        }
    }

    private void viewEmployees(ActionEvent e) {
        List<Employee> employees = MongoService.getAllEmployees();
        String[] columnNames = {
            LangUtil.get("name"),
            LangUtil.get("age"),
            LangUtil.get("department")
        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Employee emp : employees) {
            Object[] row = {emp.getName(), emp.getAge(), emp.getDepartment()};
            model.addRow(row);
        }

        employeeTable.setModel(model);
    }

    private void deleteAllEmployees(ActionEvent e) {
        // Tambahkan konfirmasi sebelum menghapus
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all employees?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            MongoService.deleteAllEmployees();
            viewEmployees(null); // Refresh tabel
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagerGUI("en"));
    }
}
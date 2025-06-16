package gui;

import app.DataProcessorThread;
import model.User;
import service.MongoService;
import util.LangUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JDialog loadingDialog;
    private JComboBox<String> languageSelector;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginBtn;
    private JButton registerBtn;

    private Locale currentLocale;

    public LoginGUI() {
        currentLocale = new Locale("en"); 
        LangUtil.setLocale("en");

        setTitle("Login App");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel atas: pemilih bahasa
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        languageSelector = new JComboBox<>(new String[]{"English", "Bahasa Indonesia"});
        languageSelector.setSelectedItem("English"); 
        languageSelector.addActionListener(e -> {
            String selected = (String) languageSelector.getSelectedItem();
            String currentLang = selected.equals("English") ? "en" : "id";
            currentLocale = new Locale(currentLang);
            LangUtil.setLocale(currentLang);
            reloadText();
        });

        topPanel.add(languageSelector);
        add(topPanel, BorderLayout.NORTH);

        // Panel tengah: form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        usernameLabel = new JLabel();
        formPanel.add(usernameLabel);
        usernameField = new JTextField();
        formPanel.add(usernameField);

        passwordLabel = new JLabel();
        formPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginBtn = new JButton();
        registerBtn = new JButton();

        loginBtn.addActionListener(this::handleLogin);
        registerBtn.addActionListener(this::handleRegister);

        formPanel.add(loginBtn);
        formPanel.add(registerBtn);

        add(formPanel, BorderLayout.CENTER);

        reloadText(); 

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void reloadText() {
        usernameLabel.setText(LangUtil.get("username"));
        passwordLabel.setText(LangUtil.get("password"));
        loginBtn.setText(LangUtil.get("login"));
        registerBtn.setText(LangUtil.get("register"));
        setTitle(LangUtil.get("login"));
        this.revalidate();
        this.repaint();
    }

    private void showLoading(String message) {
        loadingDialog = new JDialog(this, LangUtil.get("loading_title"), true);
        loadingDialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        loadingDialog.add(label, BorderLayout.NORTH);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setSize(250, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setResizable(false);
        new Thread(() -> loadingDialog.setVisible(true)).start();
    }

    private void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.setVisible(false);
            loadingDialog.dispose();
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, LangUtil.get("empty_fields"));
            return;
        }

        showLoading(LangUtil.get("registering"));

        User user = new User(username, password);
        new DataProcessorThread("Register User") {
            public void run() {
                boolean success = MongoService.registerUser(user);
                SwingUtilities.invokeLater(() -> {
                    hideLoading();
                    if (success) {
                        JOptionPane.showMessageDialog(LoginGUI.this, LangUtil.get("register_success"));
                    } else {
                        JOptionPane.showMessageDialog(LoginGUI.this, LangUtil.get("register_failed"));
                    }
                });
            }
        }.start();
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        showLoading(LangUtil.get("logging_in"));

        new DataProcessorThread("Login User") {
            public void run() {
                boolean success = MongoService.loginUser(username, password);
                SwingUtilities.invokeLater(() -> {
                    hideLoading();
                    if (success) {
                        JOptionPane.showMessageDialog(LoginGUI.this, LangUtil.get("login_success"));
                        dispose();
                        new EmployeeManagerGUI(currentLocale.getLanguage());
                    } else {
                        JOptionPane.showMessageDialog(LoginGUI.this, LangUtil.get("login_failed"));
                    }
                });
            }
        }.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}

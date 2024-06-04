package graphic;

import main.client.Client;
import utils.ConsoleAdministrator;
import utils.DataBaseManager;
import utils.Serializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginRegisterFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static boolean isOpen=false;

    public LoginRegisterFrame() {
        if (isOpen) {
            return;
        }
        setIsOpen(true);
        setTitle("Login and Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Панели для авторизации и регистрации
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        JTextField txtUsername = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JButton btnLogin = new JButton("Login");
        JButton btnSwitchToRegister = new JButton("Register");
        JButton btnClose = new JButton("Close");

        btnLogin.addActionListener(
                e -> {
                    String login = txtUsername.getText();
                    String password = new String(txtPassword.getPassword());
                    String password384 = Serializer.sha384Hash(password);
                    try {
                        if (DataBaseManager.validateUser(login, password384)) {
                            JOptionPane.showMessageDialog(LoginRegisterFrame.this, "Успешная авторизация");
                            Client.setIsAuthorisated(true);
                            setIsOpen(false);
                            Client.setCurrentUser(login);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginRegisterFrame.this, "Не верный логин или пароль");
                        }
                    } catch (SQLException | ClassNotFoundException a) {
                        System.out.println("Ошибка с базой данных при авторизации");
                    }
                });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnSwitchToRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);

        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        gbc.gridy = 3;
        panel.add(btnSwitchToRegister, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        JTextField txtUsername = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JPasswordField txtConfirmPassword = new JPasswordField(20);
        JButton btnRegister = new JButton("Register");
        JButton btnSwitchToLogin = new JButton("Login");

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                if (password.equals(confirmPassword)) {
                    String password1 = Serializer.sha384Hash(password);
                    try {
                        DataBaseManager.addUser(login, password1);
                        System.out.println("Пользователь успешно добавлен");
                        Client.setCurrentUser(login);
                        Client.setIsAuthorisated(true);
                        dispose();
                    } catch (SQLException | ClassNotFoundException a) {
                        System.out.println("Не удалось добавить пользователя - либо такой пользователь уже существует либо проблемы с базой данных");
                    }
                }else{
                    JOptionPane.showMessageDialog(LoginRegisterFrame.this, "Пароли не совпадают");
                }
        }});

        btnSwitchToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);

        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblConfirmPassword, gbc);

        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        gbc.gridy = 4;
        panel.add(btnSwitchToLogin, gbc);

        return panel;
    }

    public static boolean getIsOpen() {
        return isOpen;
    }

    public static void setIsOpen(boolean isOpen) {
        LoginRegisterFrame.isOpen = isOpen;
    }
}

package com.bank;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Account {
    private int accNo;
    private String name;
    private double balance;

    public Account(int accNo, String name, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
    }

    public int getAccNo() { return accNo; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

public class BankSystem extends JFrame {

    private static Map<Integer, Account> database = new HashMap<>();
    private static int accountCounter = 1001;

    private JTextField nameField, amountField, accNoField;
    private JTable table;
    private DefaultTableModel tableModel;

    public BankSystem() {
        // --- Window Setup ---
        setTitle("🏦 Modern Bank Management System");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        JLabel titleLabel = new JLabel("BANK MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Left Input Panel ---
        JPanel inputPanel = new JPanel(new GridLayout(9, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Operations & Details"));
        inputPanel.setPreferredSize(new Dimension(280, 0));

        inputPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Account No (for Deposit/Withdraw):"));
        accNoField = new JTextField();
        inputPanel.add(accNoField);

        inputPanel.add(new JLabel("Amount (₹):"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        // Buttons
        JButton btnCreate = new JButton("✨ Create Account");
        JButton btnDeposit = new JButton("💵 Deposit");
        JButton btnWithdraw = new JButton("💳 Withdraw");

        // Button Styling
        btnCreate.setBackground(new Color(40, 167, 69));
        btnCreate.setForeground(Color.WHITE);
        btnDeposit.setBackground(new Color(0, 123, 255));
        btnDeposit.setForeground(Color.WHITE);
        btnWithdraw.setBackground(new Color(220, 53, 69));
        btnWithdraw.setForeground(Color.WHITE);

        inputPanel.add(btnCreate);
        inputPanel.add(btnDeposit);
        inputPanel.add(btnWithdraw);

        add(inputPanel, BorderLayout.WEST);

        // --- Right Table Panel (To display all accounts) ---
        String[] columns = {"Account No", "Holder Name", "Balance (₹)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Live Customer Accounts Database"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Event Listeners ---
        btnCreate.addActionListener(e -> createAccount());
        btnDeposit.addActionListener(e -> deposit());
        btnWithdraw.addActionListener(e -> withdraw());
    }

    // --- Action Functions ---

    private void createAccount() {
        String name = nameField.getText().trim();
        String amountText = amountField.getText().trim();

        if (name.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Name and Initial Deposit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            int accNo = accountCounter++;
            Account acc = new Account(accNo, name, amount);
            database.put(accNo, acc);

            // Add row to table
            tableModel.addRow(new Object[]{accNo, name, "₹ " + amount});
            clearFields();
            JOptionPane.showMessageDialog(this, "✅ Account Created! Account No: " + accNo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deposit() {
        String accText = accNoField.getText().trim();
        String amountText = amountField.getText().trim();

        if (accText.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Account No and Amount to Deposit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int accNo = Integer.parseInt(accText);
            double amount = Double.parseDouble(amountText);

            if (database.containsKey(accNo)) {
                Account acc = database.get(accNo);
                acc.setBalance(acc.getBalance() + amount);
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "✅ Deposited ₹" + amount + " successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Account Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdraw() {
        String accText = accNoField.getText().trim();
        String amountText = amountField.getText().trim();

        if (accText.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Account No and Amount to Withdraw!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int accNo = Integer.parseInt(accText);
            double amount = Double.parseDouble(amountText);

            if (database.containsKey(accNo)) {
                Account acc = database.get(accNo);
                if (acc.getBalance() >= amount) {
                    acc.setBalance(acc.getBalance() - amount);
                    refreshTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "✅ Withdrawal Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Account Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Inputs!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Account acc : database.values()) {
            tableModel.addRow(new Object[]{acc.getAccNo(), acc.getName(), "₹ " + acc.getBalance()});
        }
    }

    private void clearFields() {
        nameField.setText("");
        amountField.setText("");
        accNoField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankSystem().setVisible(true);
        });
    }
}
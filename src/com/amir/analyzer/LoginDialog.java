package com.amir.analyzer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginDialog
    extends JDialog {

    private final JTextField tfUsername;
    private final JPasswordField pfPassword;
    private final JLabel lbUsername;
    private final JLabel lbPassword;
    private final JButton btnLogin;
    private final JButton btnCancel;
    private boolean succeeded;

    public LoginDialog(final Frame parent) {
        super(parent, "Login", true);
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.lbUsername = new JLabel("Username: ");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        panel.add(this.lbUsername, gridBagConstraints);
        this.tfUsername = new JTextField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panel.add(this.tfUsername, gridBagConstraints);
        this.lbPassword = new JLabel("Password: ");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        panel.add(this.lbPassword, gridBagConstraints);
        this.pfPassword = new JPasswordField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panel.add(this.pfPassword, gridBagConstraints);
        panel.setBorder(new LineBorder(Color.GRAY));
        this.btnLogin = new JButton("Login");

        this.btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        this.btnCancel = new JButton("Cancel");
        this.btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        final JPanel bp = new JPanel();
        bp.add(this.btnLogin);
        bp.add(this.btnCancel);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername() {
        return this.tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(this.pfPassword.getPassword());
    }

    public boolean isSucceeded() {
        return this.succeeded;
    }
}

package src.java.Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import src.java.Model.Utilisateur;
import src.java.security.Security;

class RoundedButton extends JButton {
    private static final int BORDER_RADIUS = 15;
    private static final Color DARK_BG = new Color(45, 45, 45);
    private static final Color BORDER_COLOR = Color.WHITE;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(120, 40));
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(DARK_BG);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);

        // Draw white border
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);

        // Draw text
        super.paintComponent(g);
    }
}

public class Formulaire extends JFrame {
    private static final Path DEFAULT_USERS_FILE = Path.of("src", "base", "User.env");
    private final Security security = new Security();
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel statusLabel = new JLabel(" ");
    private final List<Utilisateur> utilisateurs = new ArrayList<>();

    public Formulaire() {
        this(DEFAULT_USERS_FILE);
    }

    public Formulaire(Path usersFile) {
        chargerUtilisateurs(usersFile);

        setTitle("Verification Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        // Dark theme colors
        Color darkBg = new Color(43, 43, 43);
        Color darkPanel = new Color(55, 55, 55);
        Color textColor = Color.WHITE;

        // Style text fields
        emailField.setBackground(darkPanel);
        emailField.setForeground(textColor);
        emailField.setCaretColor(textColor);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        passwordField.setBackground(darkPanel);
        passwordField.setForeground(textColor);
        passwordField.setCaretColor(textColor);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBackground(darkBg);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setForeground(textColor);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JButton loginButton = new RoundedButton("Se connecter");
        JButton resetButton = new RoundedButton("Effacer");

        loginButton.addActionListener(event -> verifierConnexion());
        resetButton.addActionListener(event -> {
            emailField.setText("");
            passwordField.setText("");
            statusLabel.setText(" ");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(darkBg);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        statusLabel.setForeground(new Color(255, 100, 100));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(darkBg);
        centerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(darkBg);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
        getContentPane().setBackground(darkBg);
    }

    private void chargerUtilisateurs(Path usersFile) {
        try {
            List<String> lignes = Files.readAllLines(usersFile);

            for (String ligne : lignes) {
                String trimmedLine = ligne.trim();

                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmedLine.split(";");

                if (parts.length < 2) {
                    continue;
                }

                String email = parts[0].trim();
                String motDePasse = parts[1].trim();
                String role = parts.length >= 3 ? parts[2].trim() : "user";

                utilisateurs.add(new Utilisateur(email, motDePasse, role));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de charger les utilisateurs depuis " + usersFile, exception);
        }
    }

    private void verifierConnexion() {
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword());

        if (email.isEmpty() || motDePasse.isEmpty()) {
            statusLabel.setText("Veuillez saisir l'email et le mot de passe.");
            return;
        }

        Utilisateur tentative = new Utilisateur(email, motDePasse);

        if (tentative.isAdmin(utilisateurs, tentative)) {
            dispose();
            new Application(email, "admin");
            return;
        }

        if (tentative.isUtilisateur(utilisateurs, tentative)) {
            dispose();
            new Application(email, "user");
            return;
        }

        statusLabel.setText("Email ou mot de passe incorrect.");
        JOptionPane.showMessageDialog(this, "Identifiants invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

package src.java.Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import src.java.Generique.ChampFormulaire;
import src.java.Generique.ColonneTableau;
import src.java.Generique.FormulaireGenerique;
import src.java.Generique.TableauGenerique;
import src.java.Model.Utilisateur;

public class Application extends JFrame {
    private final List<Utilisateur> utilisateursGeneriques = new ArrayList<>();

    public Application(String userEmail, String role) {
        chargerUtilisateursGeneriques();

        setTitle("Milt'IT - Application");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        // Dark theme colors
        Color darkBg = new Color(43, 43, 43);
        Color darkPanel = new Color(55, 55, 55);
        Color textColor = Color.WHITE;

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(darkBg);

        // Left sidebar
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(darkPanel);
        sidebar.setPreferredSize(new Dimension(200, 600));

        // Logo
        JLabel logoLabel = new JLabel("Milt'IT");
        logoLabel.setForeground(textColor);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setBorder(new EmptyBorder(20, 0, 30, 0));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(darkPanel);
        logoPanel.add(logoLabel);

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 15));
        menuPanel.setBackground(darkPanel);
        menuPanel.setBorder(new EmptyBorder(20, 15, 0, 15));

        String[] menuItems = { "Dashboard", "Lait", "Stock", "Vente", "Formulaire généralisé", "Liste généralisé" };
        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item, textColor, darkPanel);
            menuPanel.add(menuButton);
        }

        // Logout button
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(darkPanel);
        logoutPanel.setBorder(new EmptyBorder(0, 15, 20, 15));

        JButton logoutButton = new JButton();
        logoutButton.setLayout(new BorderLayout(10, 0));
        logoutButton.setBackground(darkPanel);
        logoutButton.setForeground(textColor);
        logoutButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));

        // Add logout image and text
        try {
            File imageFile = new File("src/assets/images/logout.png");
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                JLabel logoutIconLabel = new JLabel(new ImageIcon(scaledImage));
                JLabel logoutTextLabel = new JLabel("Se déconnecter");
                logoutTextLabel.setForeground(textColor);
                logoutTextLabel.setFont(new Font("Arial", Font.BOLD, 12));

                JPanel logoutContentPanel = new JPanel(new BorderLayout(10, 0));
                logoutContentPanel.setBackground(darkPanel);
                logoutContentPanel.add(logoutIconLabel, BorderLayout.WEST);
                logoutContentPanel.add(logoutTextLabel, BorderLayout.CENTER);

                logoutButton.add(logoutContentPanel, BorderLayout.CENTER);
            } else {
                logoutButton.setText("Se déconnecter");
            }
        } catch (Exception e) {
            logoutButton.setText("Se déconnecter");
        }

        logoutButton.addActionListener(event -> logout());

        logoutPanel.add(logoutButton, BorderLayout.SOUTH);

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(darkBg);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        String greeting = "Bonjour, " + (role.equals("admin") ? "administrateur" : "utilisateur");
        JLabel greetingLabel = new JLabel(greeting);
        greetingLabel.setForeground(textColor);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));

        contentPanel.add(greetingLabel, BorderLayout.NORTH);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void chargerUtilisateursGeneriques() {
        Path usersFile = Path.of("src", "base", "User.env");

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

                utilisateursGeneriques.add(new Utilisateur(email, motDePasse, role));
            }
        } catch (IOException exception) {
            utilisateursGeneriques.add(new Utilisateur("demo@mail.com", "demo123", "user"));
            utilisateursGeneriques.add(new Utilisateur("admin@mail.com", "admin123", "admin"));
        }
    }

    private JButton createMenuButton(String text, Color textColor, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(event -> ouvrirSection(text));
        return button;
    }

    private void ouvrirSection(String section) {
        if ("Formulaire généralisé".equals(section)) {
            ouvrirFormulaireGenerique();
            return;
        }

        if ("Liste généralisé".equals(section)) {
            ouvrirTableauGenerique();
        }
    }

    private void ouvrirFormulaireGenerique() {
        List<ChampFormulaire> champs = List.of(
                new ChampFormulaire("email", "Email"),
                new ChampFormulaire("motDePasse", "Mot de passe", true),
                new ChampFormulaire("role", "Rôle"));

        FormulaireGenerique<Utilisateur> formulaire = new FormulaireGenerique<>(
                this,
                "Formulaire généralisé",
                champs,
                valeurs -> {
                    String email = valeurs.get("email");
                    String motDePasse = valeurs.get("motDePasse");
                    String role = valeurs.getOrDefault("role", "user");

                    if (email.isEmpty() || motDePasse.isEmpty()) {
                        return null;
                    }

                    return new Utilisateur(email, motDePasse, role.isEmpty() ? "user" : role);
                },
                utilisateur -> {
                    utilisateursGeneriques.add(utilisateur);
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur créé : " + utilisateur.getEmail() + " (" + utilisateur.getRole() + ")",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                });

        formulaire.setVisible(true);
    }

    private void ouvrirTableauGenerique() {
        List<ColonneTableau<Utilisateur>> colonnes = List.of(
                new ColonneTableau<>("Email", Utilisateur::getEmail),
                new ColonneTableau<>("Rôle", Utilisateur::getRole));

        TableauGenerique<Utilisateur> tableau = new TableauGenerique<>(
                "Liste généralisée",
                colonnes,
                utilisateursGeneriques);

        tableau.setVisible(true);
    }

    private void logout() {
        dispose();
        new Formulaire();
    }
}

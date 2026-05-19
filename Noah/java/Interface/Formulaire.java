package Noah.java.Interface;

import Noah.java.Model.Utilisateur;
import Noah.java.security.Security;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Formulaire extends JFrame {
    private static final Path DEFAULT_USERS_FILE = Path.of("Noah", "base", "User.env");
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
        setSize(360, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.add(new JLabel("Email :"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Mot de passe :"));
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Se connecter");
        JButton resetButton = new JButton("Effacer");

        loginButton.addActionListener(event -> verifierConnexion());
        resetButton.addActionListener(event -> {
            emailField.setText("");
            passwordField.setText("");
            statusLabel.setText(" ");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
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
            statusLabel.setText("Connexion reussie : compte admin.");
            JOptionPane.showMessageDialog(this, "Bienvenue administrateur !");
            return;
        }

        if (tentative.isUtilisateur(utilisateurs, tentative)) {
            statusLabel.setText("Connexion reussie : utilisateur.");
            JOptionPane.showMessageDialog(this, "Bienvenue utilisateur !");
            return;
        }

        statusLabel.setText("Email ou mot de passe incorrect.");
        JOptionPane.showMessageDialog(this, "Identifiants invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

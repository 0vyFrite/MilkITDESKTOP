package src.java.Generique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Formulaire réutilisable qui transforme les champs saisis en objet métier.
 */
public class FormulaireGenerique<T> extends JDialog {
    private final ConvertisseurFormulaire<T> convertisseur;
    private final ActionApresValidation<T> auValidation;
    private final Map<String, JTextField> champs = new LinkedHashMap<>();

    public FormulaireGenerique(Frame parent,
            String titre,
            List<ChampFormulaire> definitions,
            ConvertisseurFormulaire<T> convertisseur,
            ActionApresValidation<T> auValidation) {
        super(parent, titre, true);
        this.convertisseur = convertisseur;
        this.auValidation = auValidation;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel racine = new JPanel(new BorderLayout(0, 15));
        racine.setBackground(ThemeGenerique.DARK_BG);
        racine.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formulaire = new JPanel(new GridLayout(definitions.size(), 2, 10, 10));
        formulaire.setBackground(ThemeGenerique.DARK_BG);
        for (ChampFormulaire definition : definitions) {
            JLabel label = new JLabel(definition.getLibelle());
            label.setForeground(ThemeGenerique.TEXT);
            label.setFont(new Font("Arial", Font.BOLD, 13));

            JTextField champ = definition.isSecret() ? new JPasswordField() : new JTextField();
            ThemeGenerique.styleField(champ);
            champ.setPreferredSize(new Dimension(220, 30));
            champs.put(definition.getCle(), champ);

            formulaire.add(label);
            formulaire.add(champ);
        }

        JPanel actions = new JPanel();
        actions.setBackground(ThemeGenerique.DARK_BG);
        JButton valider = ThemeGenerique.createButton("Valider");
        JButton annuler = ThemeGenerique.createButton("Annuler");

        valider.addActionListener(event -> validerFormulaire());
        annuler.addActionListener(event -> dispose());

        actions.add(valider);
        actions.add(annuler);

        racine.add(formulaire, BorderLayout.CENTER);
        racine.add(actions, BorderLayout.SOUTH);

        add(racine);
        getContentPane().setBackground(ThemeGenerique.DARK_BG);
    }

    private void validerFormulaire() {
        Map<String, String> valeurs = recupererValeurs();
        T objet = convertisseur.convertir(valeurs);

        if (objet == null) {
            JOptionPane.showMessageDialog(this, "Les valeurs saisies sont invalides.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        auValidation.executer(objet);
        dispose();
    }

    private Map<String, String> recupererValeurs() {
        Map<String, String> valeurs = new LinkedHashMap<>();

        for (Map.Entry<String, JTextField> entree : champs.entrySet()) {
            JTextField champ = entree.getValue();
            String valeur = champ instanceof JPasswordField
                    ? new String(((JPasswordField) champ).getPassword())
                    : champ.getText();
            valeurs.put(entree.getKey(), valeur.trim());
        }

        return valeurs;
    }
}
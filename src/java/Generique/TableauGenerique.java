package src.java.Generique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Tableau réutilisable pour afficher une liste d'objets.
 */
public class TableauGenerique<T> extends JFrame {
    private final List<ColonneTableau<T>> colonnes;
    private final DefaultTableModel modele;

    public TableauGenerique(String titre, List<ColonneTableau<T>> colonnes, List<T> elements) {
        super(titre);
        this.colonnes = new ArrayList<>(colonnes);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ThemeGenerique.DARK_BG);

        String[] titresColonnes = this.colonnes.stream().map(ColonneTableau::getTitre).toArray(String[]::new);
        modele = new DefaultTableModel(titresColonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableau = new JTable(modele);
        tableau.setFillsViewportHeight(true);
        tableau.setBackground(ThemeGenerique.DARK_PANEL);
        tableau.setForeground(ThemeGenerique.TEXT);
        tableau.setGridColor(new Color(90, 90, 90));
        tableau.setRowHeight(26);
        tableau.setShowHorizontalLines(true);
        tableau.setShowVerticalLines(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(ThemeGenerique.DARK_PANEL);
        renderer.setForeground(ThemeGenerique.TEXT);
        for (int i = 0; i < tableau.getColumnCount(); i++) {
            tableau.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = tableau.getTableHeader();
        header.setBackground(ThemeGenerique.BUTTON_BG);
        header.setForeground(ThemeGenerique.TEXT);
        header.setFont(header.getFont().deriveFont(java.awt.Font.BOLD));

        add(new JScrollPane(tableau), BorderLayout.CENTER);

        remplir(elements);
    }

    public final void remplir(List<T> elements) {
        modele.setRowCount(0);

        for (T element : elements) {
            Object[] ligne = new Object[colonnes.size()];

            for (int i = 0; i < colonnes.size(); i++) {
                ligne[i] = colonnes.get(i).getExtracteur().apply(element);
            }

            modele.addRow(ligne);
        }
    }
}
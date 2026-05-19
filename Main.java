import javax.swing.SwingUtilities;

import Noah.java.Interface.Formulaire;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Formulaire().setVisible(true));
    }
}

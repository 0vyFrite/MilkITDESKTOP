package src.java.Generique;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 * Couleurs et petits helpers partagés pour garder le même design partout.
 */
public final class ThemeGenerique {
    public static final Color DARK_BG = new Color(43, 43, 43);
    public static final Color DARK_PANEL = new Color(55, 55, 55);
    public static final Color TEXT = Color.WHITE;
    public static final Color FIELD_BORDER = new Color(100, 100, 100);
    public static final Color BUTTON_BG = new Color(45, 45, 45);

    private ThemeGenerique() {
    }

    public static void styleField(JTextField field) {
        field.setBackground(DARK_PANEL);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createLineBorder(FIELD_BORDER, 1));
    }

    public static RoundedButton createButton(String text) {
        return new RoundedButton(text);
    }

    public static class RoundedButton extends JButton {
        private static final int BORDER_RADIUS = 15;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setFont(new Font("Arial", Font.BOLD, 14));
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BUTTON_BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
            g2.setColor(Color.WHITE);
            g2.setStroke(new java.awt.BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
            super.paintComponent(g);
        }
    }
}
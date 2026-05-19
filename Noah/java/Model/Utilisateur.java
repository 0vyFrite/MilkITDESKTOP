package Noah.java.Model;

import Noah.java.security.Security;

import java.util.List;

public class Utilisateur {
    private final Security security = new Security();
    private String email;
    private String motDePasse;
    private String role;

    public Utilisateur(String email, String motDePasse) {
        this(email, motDePasse, "user");
    }

    public Utilisateur(String email, String motDePasse, String role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getRole() {
        return role;
    }

    public boolean isUtilisateur(List<Utilisateur> utilisateurs, Utilisateur utilisateur) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equals(this.email)
                    && u.getMotDePasse().equals(security.hashPassword(this.motDePasse))) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(List<Utilisateur> utilisateurs, Utilisateur utilisateur) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equals(this.email)
                    && u.getMotDePasse().equals(security.hashPassword(this.motDePasse))
                    && "admin".equalsIgnoreCase(u.getRole())) {
                return true;
            }
        }
        return false;
    }
}
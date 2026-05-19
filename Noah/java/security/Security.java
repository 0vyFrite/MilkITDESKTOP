package Noah.java.security;

import org.mindrot.jbcrypt.BCrypt;

public class Security {
    public String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public boolean checkPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }

        return BCrypt.checkpw(password, hashedPassword);
    }
}

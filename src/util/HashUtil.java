package util;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String hashBCrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkBCrypt(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}

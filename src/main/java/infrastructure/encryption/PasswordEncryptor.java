package infrastructure.encryption;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class PasswordEncryptor {


    public static String encrypt(String text) {
        if(text==null){
            throw new IllegalArgumentException("Text is required");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            //It cannot happen
            throw new IllegalStateException(e);
        }

    }
}

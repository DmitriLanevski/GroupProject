package serverDatabase;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
/**
 * Created by Stas on 17/04/2016.
 */
public class Encryption {
    private static String instanceId = "PBKDF2WithHmacSHA1";
    private static byte[] salt = "J8/h(kH6fH6¤6#0L".getBytes();

    // Returns Password-Hash
    public static String encrypt(String pPassword) throws Exception {
        KeySpec spec = new PBEKeySpec(pPassword.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance(instanceId);
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hash);
    }

    public static boolean check(String pPassword, String pHash) throws Exception {
        // System.out.println("... debug:   pHash="+pHash);
        String theHash = encrypt(pPassword);
        // System.out.println("... debug: theHash="+theHash);
        if (theHash.equals(pHash)) return true;
        return false;
    }

   public static void main(String[] args) throws Exception {
        String thePassword = "My Password To Test";
        System.out.println("Password to test: "+thePassword);
        String theHash = encrypt(thePassword);
        System.out.println("The hash to store in database: "+theHash);
        System.out.println("Result of checking of pair thePassword + theHash:");
        if (check(thePassword, theHash))
            System.out.println("... function check returns TRUE");
        else
            System.out.println("... function check returns FALSE");
    }
}
package serverDatabase;

/**
 * Created by Stas on 09/05/2016.
 */
public class CrypterTest {
    public static void main(String[] args) {
        String hashed = Bcrypt.hashpw("12345",Bcrypt.gensalt());
        System.out.println("hashed PW : "+hashed);
        String hashed2 = Bcrypt.hashpw("12346",Bcrypt.gensalt());
        System.out.println("Stuff : "+hashed2);

        if (Bcrypt.checkpw(hashed, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
}

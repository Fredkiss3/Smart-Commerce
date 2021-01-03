package corp.fredkiss.smart_commerce.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String mName;
    private String mPassword;

    /**
     * instancier un nouvel 'utilisateur' de l'application
     */
    public User() {
        this("", "");
    }

    /**
     * instancier un nouvel 'utilisateur' de l'application
     * @param name nom
     * @param password mot de passe
     */
    public User(String name, String password) {
        mName = name;

        // le mot de passe est enregistré de manière cryptée
        this.mPassword = encryptPassword(password);
    }

    /**
     * chiffrer le mot de passe en utilisant l'algorithme de MD5
     * @param password le mot de passe
     */
    private String encryptPassword(String password) throws RuntimeException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] passDigest = md5.digest(password.getBytes());
            BigInteger number = new BigInteger(1, passDigest);
            StringBuilder encrypted = new StringBuilder(number.toString());

            while (encrypted.length() < 32){
                encrypted.insert(0, "0");
            }

            return encrypted.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Vérifier un mot de passe
     * @param password le mot de passe
     */
    public boolean verifyEncryptedPassword(String password){
        try{
            if(encryptPassword(password).equals(this.mPassword)){
                return true;
            }
        }catch (RuntimeException ignore){}
        return false;
    }

    /**
     *
     * @param name le nom
     */
    public void setName(String name) {
        mName = name.toLowerCase().trim();
    }

    /**
     *
     * @return le nom
     */
    public String getName() {
        return mName;
    }

    /**
     * affecter une valeur au mot de passe
     * @param password le mot de passe
     */
    public void setPassword(String password) {
        mPassword = password;
    }

    /**
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return mPassword;
    }
}

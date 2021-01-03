package corp.fredkiss.smart_commerce.controller;

import android.content.SharedPreferences;

import corp.fredkiss.smart_commerce.model.User;

public final class ControlUser {
    private static ControlUser ourInstance = null;
    private static User ourUser = null;
    private SharedPreferences mSharedPreferences;
    private static final String NAMEKEY = "USERNAME";
    private static final String PASSKEY = "PASSWORD";
    private boolean mIsFirstTime;

    /**
     * créer un nouvel utilisateur
     * @param name nom d'utilisateur
     * @param password mot de passe
     * @return vrai si tout a marché, faux pour le contraire
     */
    public boolean createUser(String name, String password) {
        try {
            ourUser = new User(name, password);
            saveUserPreferences();
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }

    public String getUserName() throws Exception {

        // TODO : TO CHANGE
        ourUser = new User();
        ourUser.setName("FREDHEL KISSIE");

        if(ourUser != null)
            return ourUser.getName();
        else
            throw new Exception("Aucun utilisateur enregistré !");
    }

    /**
     * récupérer l'instance du controleur
     * @return l'instance
     */
    public static ControlUser getInstance(SharedPreferences pref) {
        if(ourInstance == null){
            ourInstance = new ControlUser(pref);
        }
        return ourInstance;
    }

    /**
     * constructeur
     * @param pref les préférences
     */
    private ControlUser(SharedPreferences pref) {
        mSharedPreferences = pref;
        getUserPreferences();
    }

    /**
     * Récupérer les préférences de l'utilisateur
     */
    private void getUserPreferences(){
        String name = mSharedPreferences.getString(NAMEKEY, null);
        String password = mSharedPreferences.getString(PASSKEY, null);

        if(name == null || password == null){
            mIsFirstTime = true;
        }else {
            ourUser = new User();
            ourUser.setPassword(password);
            ourUser.setName(name);
            mIsFirstTime = false;
        }
    }

    /**
     * Sauvegarder les préférences de l'utilisateur
     */
    private void saveUserPreferences(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        // sauvegarde effective
        editor.putString(NAMEKEY, ourUser.getName())
                .putString(PASSKEY, ourUser.getPassword())
                .apply();
        // mise à jour des paramètres
        getUserPreferences();
    }

    /**
     * Vérifier le nom d'utilisateur et le mot de passe
     * @param name
     * @param password
     * @return vrai si l'utilisateur est bien le bon, faux dans le cas contraire
     */
    public boolean verifyUser(String name, String password){
        return (ourUser.getName().equals(name.toLowerCase().trim())) && (ourUser.verifyEncryptedPassword(password));
    }

    /**
     * Pour savoir si c'est la première fois
     * @return
     */
    public boolean isFirstTime() {
        return mIsFirstTime;
    }
}

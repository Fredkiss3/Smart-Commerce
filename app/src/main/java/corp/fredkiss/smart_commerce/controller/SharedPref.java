package corp.fredkiss.smart_commerce.controller;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPref {
    private static SharedPref ourInstance = null;
    private SharedPreferences mypref;

    public static SharedPref getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new SharedPref(context);
        }
        return ourInstance;
    }

    private SharedPref(Context context) {
        mypref = context.getSharedPreferences("fd_45879", Context.MODE_PRIVATE);
    }

    /**
     * Récupérer l'état du mode nuit
     * @return l'état
     */
    public boolean loadNightMode() {
        return mypref.getBoolean("NIGHT_MODE", false);
    }

    /**
     * Enregistrer l'état du mode nuit
     * @param state l'état
     */
    public void saveNightMode(Boolean state) {
        SharedPreferences.Editor editor = mypref.edit()
                .putBoolean("NIGHT_MODE", state);
        editor.apply();
    }

    /**
     * Enregistrer un utilisateur
     * @param nameKey la clé de sauvegarde de l'utilisateur
     * @param name le nom de l'utilisateur
     * @param passKey la clé de sauvegarde du mot de passe de l'utlilisateur
     * @param password le mot de passe de l'utlilisateur
     */
    public void saveUser(String nameKey, String name, String passKey, String password) {
        SharedPreferences.Editor editor = mypref.edit();

        // sauvegarde effective
        editor.putString(nameKey, name)
                .putString(passKey, password)
                .apply();
    }

    /**
     * Récupérer la valeur d'une clé
     * @param key la clé
     * @return la valeur
     */
    public String getStringValue(String key) {
        return mypref.getString(key, null);
    }

}

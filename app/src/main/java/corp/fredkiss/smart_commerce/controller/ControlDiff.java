package corp.fredkiss.smart_commerce.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import corp.fredkiss.smart_commerce.model.Crediteur;
import corp.fredkiss.smart_commerce.model.MyDBHelper;

public final class ControlDiff {
    private List<Crediteur> ourCrediteurList;
    private final MyDBHelper mDBHelper;
    private static ControlDiff ourInstance = null;

    /**
     * @return l'instance
     */
    public static ControlDiff getInstance(Context contexte) {
        if (ControlDiff.ourInstance == null) {
            ourInstance = new ControlDiff(contexte);
        }
        return ourInstance;
    }

    /**
     * Constructeur
     */
    private ControlDiff(Context contexte) {
        mDBHelper = new MyDBHelper(contexte);
        getAllCred();
    }

    /**
     * Récupérer tous les créditeurs
     */
    private void getAllCred(){
        Cursor cur = mDBHelper.getAllData("Crediteur", "id");
        ourCrediteurList = new ArrayList<>();

        while (cur.moveToNext()){
            // parcourir le curseur et ajouter des 'créditeurs' à la 'liste'
            Crediteur cred = new Crediteur(cur.getString(2), cur.getString(3),cur.getInt(1));
            cred.setIdCred(cur.getInt(0));
            ourCrediteurList.add(cred);
        }
    }

    /**
     *
     * @return le nombre de créditeurs
     */
    public int getCount() {
        return ourCrediteurList.size();
    }

    /**
     * ajouter un créditeur à la BDD
     * @param chbreClient chambre
     * @param nomClient  nom
     * @param somDiff somme
     */
    public boolean addCrediteur(String nomClient, String chbreClient, int somDiff){
        if(somDiff == 0)
            return false;

        Crediteur crediteur = new Crediteur(nomClient, chbreClient, somDiff);
        boolean res = mDBHelper.insertData("Crediteur", crediteur.getAllData());

        // mise à jour du nombre de créditeurs
        getAllCred();
        return res;
    }

    /**
     * Enlever un créditeur de la BDD
     * @param id son identifiant
     */
    public boolean removeCrediteur(int id){
        if(id == 0)
            return false;

        boolean res = mDBHelper.removeData("id", id, "Crediteur");

        // mise à jour du nombre de créditeurs
        getAllCred();

        return res;
    }

    /**
     * Modifier un créditeur
     * @param id l'id du créditeur
     */
    public boolean modifyCrediteur(int id, String nomClient, String chbreClient, int somDiff){
        // la somme doit être > 0
        if(somDiff == 0)
            return false;

        Crediteur crediteur = new Crediteur(nomClient, chbreClient, somDiff);
        crediteur.setIdCred(id);
        boolean res = mDBHelper.updateData("Crediteur", "id",
                String.valueOf(id), crediteur.getAllData());

        // mise à jour du nombre de créditeurs
        getAllCred();

        return res;
    }

    /**
     * Récupérer un créditeur
     * @param id son identifiant
     * @return la liste des attributs du créditeur
     * @throws ElementNotExistException si la liste est vide (élément non existant dans la liste des créditeurs)
     */
    public List<String> getCrediteurById(int id) throws ElementNotExistException {
        Crediteur cred = null;
        for (int i=0; i < ourCrediteurList.size() ; i++) {
            if (ourCrediteurList.get(i).getIdCred() == id){
                cred = ourCrediteurList.get(i);
            }
        }

        List<String> credAttibutes = new ArrayList<>();

        if(cred != null){
            credAttibutes.add( String.valueOf(cred.getIdCred()) ); // id
            credAttibutes.add(cred.getNomClient()); // nom
            credAttibutes.add(cred.getChbreClient()); // Chambre
            credAttibutes.add( String.valueOf(cred.getSomDiff()) ); // somme
        }

        if(credAttibutes.size() == 0){
            throw new ElementNotExistException("Erreur lors de la récupération de l'attribut du client !");
        }else {
            return credAttibutes;
        }
    }

    /**
     * Récupérer un créditeur
     * @param i son indice dans la liste
     * @return la liste des attributs du créditeur
     * @throws ElementNotExistException si la liste est vide (élément non existant dans la liste des créditeurs)
     */
    public Map<String, String> getCrediteurByIndex(int i) throws ElementNotExistException {
        Crediteur cred = ourCrediteurList.get(i);

        // les données du créditeur
        Map<String, String> credAttibutes = cred.getAllData();
        credAttibutes.put(cred.getIdName(), String.valueOf(cred.getIdCred()));

        if(credAttibutes.size() == 0){
            throw new ElementNotExistException("Erreur lors de la récupération de l'attribut du client !");
        }else {
            return credAttibutes;
        }
    }

    public String getNomCred(int pos){
        return ourCrediteurList.get(pos).getNomClient();
    }

    public String getChbreCred(int pos){
        return ourCrediteurList.get(pos).getChbreClient();
    }

    public int getSomCred(int pos){
        return ourCrediteurList.get(pos).getSomDiff();
    }

    public int getIdCred(int pos){
        return ourCrediteurList.get(pos).getIdCred();
    }

}


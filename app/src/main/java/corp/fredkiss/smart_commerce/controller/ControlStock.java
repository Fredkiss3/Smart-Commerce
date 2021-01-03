package corp.fredkiss.smart_commerce.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import corp.fredkiss.smart_commerce.model.Article;
import corp.fredkiss.smart_commerce.model.Buyable;
import corp.fredkiss.smart_commerce.model.Combo;
import corp.fredkiss.smart_commerce.model.IncorrectCompositionException;
import corp.fredkiss.smart_commerce.model.MyDBHelper;

/**
 * Contrôleur permettant d'avoir un état sur le stock actuel ainsi
 * que les informations sur les différents articles
 */
public final class ControlStock {
    private static ControlStock ourInstance = null;
    private MyDBHelper mDBHelper;

    // éléments en stock
    private List<Buyable> mBuyableList;

    /**
     * Constructeur
     */
    private ControlStock(Context contexte) {
        mDBHelper = new MyDBHelper(contexte);
        getAllBuyables();
    }

    /**
     * Récupérer tout le stock
     */
    private void getAllBuyables() {
        // Liste des éléments
        mBuyableList = new ArrayList<>();

        // Tables de combos & tables des articles
        Map<String, Article> articleTable = new Hashtable<>();
        Map<String, Combo> comboTable = new Hashtable<>();

        // Récupérer les Articles
        Cursor cur = mDBHelper.getAllData("Article", "nomProd");

        while (cur.moveToNext()){
            // parcourir le curseur et ajouter des articles à la table
            String ref = cur.getString(0);
            String nom = cur.getString(1);
            int prixU = cur.getInt(2);
            int stock = cur.getInt(3);

            // Majuscules
            nom = capitalizeWord(nom);

            // TODO : PRENDRE EN COMPTE LE PRIX D'ACHATS UNITAIRE
            Article article = new Article(ref, nom, 0, stock);
            article.setPrixU(prixU);
            articleTable.put(article.getRef(), article);
        }

        // Récupérer les combos
        cur = mDBHelper.getAllData("Combo", "nomCombo");

        while (cur.moveToNext()) {
            // parcourir le curseur et ajouter des combos à la table
            Combo combo = new Combo(cur.getString(0), cur.getString(1));
            comboTable.put(combo.getRef(), combo);
        }

        // récupérer les tables de compositions
        cur = mDBHelper.getAllData("Composition", "id");

        while (cur.moveToNext()) {
            // récupérer la référence de l'article dans la composition
            String refA = cur.getString(1);

            // récupérer la référence du combo dans la composition
            String refC = cur.getString(2);

            // Récupérer la quantité de l'article dans la composition
            int qtite = cur.getInt(3);

            // récupérer le combo correspondant
            Combo combo = comboTable.get(refC);

            // récupérer donc l'article correspondant
            Article article = articleTable.get(refA);

            try {
                // ajouter l'article au combo
                combo.addArticle(article, qtite);
            } catch (IncorrectCompositionException e) {
                e.printStackTrace();
            }
        }

        // Ajouter les combos
        for (Map.Entry<String, Combo> e: comboTable.entrySet()) {
            mBuyableList.add(e.getValue());
        }

        // Ajouter les articles
        for (Map.Entry<String, Article> e: articleTable.entrySet()) {
            mBuyableList.add(e.getValue());
        }
    }

    /**
     * @return l'instance
     */
    public static ControlStock getInstance(Context contexte) {
        if (ControlStock.ourInstance == null) {
            ourInstance = new ControlStock(contexte);
        }
        return ourInstance;
    }


    /**
     * Récupérer le nombre d'éléments
     * @return le nombre d'éléments
     */
    public int getCount(){
        return mBuyableList.size();
    }

    /**
     * Récupérer le nom d'un élément
     * @param pos la position
     * @return le nom
     */
    public String getBuyableName(int pos){
        return mBuyableList.get(pos).getName();
    }

    /**
     * Récupérer le stock d'un élément
     * @param pos la position de l'élément dans la liste
     * @return le stock
     */
    public int getBuyableStock(int pos){
        return mBuyableList.get(pos).getStock();
    }

    /**
     * Récupérer le prix unitaire d'un élément dans la liste
     * @param pos la position de l'élément dans la liste
     * @return le prix
     */
    public int getBuyableCost(int pos){
        return mBuyableList.get(pos).getCost();
    }

    /**
     * Savoir si un élément est un combo
     * @param pos la position de l'élément dans la liste
     * @return vrai s'il s'agit d'un combo
     */
    public boolean isCombo(int pos){
        return mBuyableList.get(pos).getClass() == Combo.class;
    }

    /**
     * Récupérer le nombre d'article du combo
     * @param pos position de l'élément dans la liste
     * @return le nombre
     */
    public int getComboCount(int pos) throws IllegalAccessException {
        if(isCombo(pos))
            return ((Combo)mBuyableList.get(pos)).count();
        else
            throw new IllegalAccessException("tentative d'accès à un attribut inexistant dans " +
                    "la  classe");
    }

    /**
     * Récupérer la composition d'un combo
     * @param pos la position du combo dans la liste
     * @return la composition sous forme de table : <br><br>
     *     - key : nom de l'article <br>
     *     - value : nombres d'unités de l'article contenu dans le combo
     * @throws IllegalAccessException Si l'article n'est pas un combo
     */
    public Map<String, Integer> getComboComposition(int pos) throws IllegalAccessException {
        if(isCombo(pos)) {
            Map<Article, Integer> compositionTable = ((Combo)mBuyableList.get(pos)) .getComPositionTable();
            Map<String, Integer> comboComposition = new Hashtable<>();

            // ajouter le nom d'un article ainsi que sa quantité dans la composition de l'article
            for (Map.Entry<Article, Integer> e : compositionTable.entrySet()) {
                comboComposition.put(e.getKey().getName(), e.getValue());
            }
            return comboComposition;
        } else {
            throw new IllegalAccessException("tentative d'accès à un attribut inexistant dans " +
                    "la  classe");
        }
    }

    /**
     * Récupérer la référence d'un 'Buyable'
     * @param pos la position
     * @return la référence
     */
    public String getBuyableRef(int pos){
        return mBuyableList.get(pos).getRef();
    }

    /**
     * Mdofier un les infos d'un article
     * @param pos la position de l'article dans la liste des 'Buyables'
     * @param newName le nouveau nom de l'article
     * @param newPrice le nouveau prix de vente de l'article
     * @return - 'true' si la modification a bien été effectuée <br>
     *         - 'false' si la modification a échoué ou que l'article est un Combo
     */
    public boolean updateArticle(int pos, String newName, int newPrice){
        boolean modified = false;

        if(!isCombo(pos))
        {
            Article article = (Article) mBuyableList.get(pos);
            article.setPrixU(newPrice);
            article.setNomProd(newName);
            modified =  mDBHelper.updateData("Article", article.getIdName(), article.getRef(), article.getAllData());
        }
        getAllBuyables();
        return modified;
    }

    /**
     * Recharger la Base de données
     */
    public void reload() {
        getAllBuyables();
    }

    /**
     * Mettre la première lettre d'un mot en majuscule
     * @param word le mot
     * @return le mot modifié
     */
    private String capitalizeWord(String word) {
        Character first = word.charAt(0);
        return word.replaceFirst(first.toString(), first.toString().toUpperCase());
    }
}

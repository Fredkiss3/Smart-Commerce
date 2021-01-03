package corp.fredkiss.smart_commerce.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import corp.fredkiss.smart_commerce.model.Article;
import corp.fredkiss.smart_commerce.model.Combo;
import corp.fredkiss.smart_commerce.model.Composition;
import corp.fredkiss.smart_commerce.model.IncorrectCompositionException;
import corp.fredkiss.smart_commerce.model.MyDBHelper;

/**
 * Contrôleur intervenant lors de la création ou modification des combos
 */
public final class ControlCombo {
    private static ControlCombo ourInstance = null;
    private MyDBHelper mDBHelper;

    // éléments en stock
    private List<Article> mArticleList;
    private List<Combo> mComboList;
    private int mCompositionCurId;

    /**
     * Constructeur
     */
    private ControlCombo(Context contexte) {
        mDBHelper = new MyDBHelper(contexte);
        getAllBuyables();
    }

    /**
     * Récupérer tous les éléments
     */
    private void getAllBuyables() {
        // Liste des éléments
        mComboList = new ArrayList<>();
        mArticleList = new ArrayList<>();

        // Tables de combos & tables des articles
        Map<String, Article> articleTable = new Hashtable<>();
        Map<String, Combo> comboTable = new Hashtable<>();

        // Récupérer les Articles
        Cursor cur = mDBHelper.getAllData("Article", "nomProd");

        while (cur.moveToNext()) {
            // parcourir le curseur et ajouter des articles à la table
            Article article = new Article(cur.getString(0), cur.getString(1), cur.getInt(2), cur.getInt(3));
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

        // l'id de la composition
        int id = 0;

        while (cur.moveToNext()) {

            // récupérer l'id courante
            id = cur.getInt(0);

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
                combo.addArticle(article, qtite, id, false);
            } catch (IncorrectCompositionException e) {
                e.printStackTrace();
            }

            // Ajouter les combos
            for (Map.Entry<String, Combo> e : comboTable.entrySet()) {
                mComboList.add(e.getValue());
            }

            // Ajouter les articles
            for (Map.Entry<String, Article> e : articleTable.entrySet()) {
                mArticleList.add(e.getValue());
            }

        }

        // augmenter l'id de composition courante
        mCompositionCurId = ++id;
    }

    /**
     * @return l'instance
     */
    public static ControlCombo getInstance(Context contexte) {
        if (ControlCombo.ourInstance == null) {
            ourInstance = new ControlCombo(contexte);
        }
        return ourInstance;
    }

    /**
     * Ajouter un nouveau combo
     * @param nom le nom
     * @return vrai si
     */
    public boolean addCombo(String nom) {
        String ref;
        boolean isPresent = false;

        // vérfier qu'un combo du même nom n'existe pas déjà
        for (Combo c: mComboList) {
            if(c.getName().equalsIgnoreCase(nom)) {
                return false;
            }
        }

        // générer une référence qui n'est pas présente dans la liste des combos
        do {
            ref = Combo.generateRef();

            for (Combo c: mComboList) {
                if(c.getRef().equals(ref)) {
                    isPresent = true;
                }
            }
        } while (isPresent);

        // ajouter le combo à la liste
        Combo c = new Combo(ref, nom);
        mComboList.add(c);
        return true;
    }

    /**
     * Ajouter un article à la composition d'un combo
     * @param posCombo la position du combo dans la liste de combos
     * @param posArticle la position de l'article dans la liste d'articles
     * @param qtite la quantité
     * @return vrai si tout s'est bien passé
     */
    public boolean addArticle(int posCombo, int posArticle, int qtite){
        boolean added = false;
        Combo combo = mComboList.get(posCombo);
        Article article = mArticleList.get(posArticle);

        try {
            added = combo.addArticle(article, qtite, mCompositionCurId++, true);
        } catch (IncorrectCompositionException e) {
            e.printStackTrace();
            return false;
        }
        return added;
    }

    /**
     * Modifier un article
     * @param posCombo la position du combo dans la liste
     * @param posArticle la position de l'article dans la liste
     * @param newQtite la nouvelle quantité
     * @return vrai si l'article est bien présent
     */
    public boolean modifyArticle(int posCombo, int posArticle, int newQtite) {
        Combo combo = mComboList.get(posCombo);
        Article article = mArticleList.get(posArticle);
        return  combo.updateArticle(article.getRef(), newQtite);
    }

    /**
     * retirer un article de la composition d'un combo
     * @param posCombo la position du combo dans la liste
     * @param posArticle la position de l'article dans la liste
     * @return vrai si l'article est bien présent
     */
    public boolean removeArticle(int posCombo, int posArticle) {
        Combo combo = mComboList.get(posCombo);
        Article article = mArticleList.get(posArticle);
        return combo.removeArticle(article.getRef());
    }

    /**
     * Enregistrer les modifications
     * @return vrai si l'ensemble des opérations au niveau de la BDD a réussi
     * @throws Exception si une erreur intervient
     */
    public boolean save() throws Exception {
        boolean res;

        for (Combo combo: mComboList) {
            Map<String, String> comboData = combo.getAllData();

            // ajouter un combo créé si sa table de composition contient au moins un produit
            if (combo.isNew() && combo.getCompositionList().size() > 0) {
                res = mDBHelper.insertData("Combo", comboData);
                if (!res)
                    throw new Exception("Erreur d'insertion du combo dans la Base de données !");
            }

            // ajouter les informations du panier
            for (Composition compo: combo.getCompositionList()) {
                Map<String, String> compoData = compo.getAllData();

                if(combo.isNew() || compo.isNew()) {
                    // s'il s'agit d'un nouveau combo -> ajouter l'ensemble des compositions
                    res = mDBHelper.insertData("Composition", compoData);
                    mCompositionCurId++;
                } else {
                    // sinon, mettre à jour ses compositions
                    res = mDBHelper.updateData("Composition", compo.getIdName(),
                            String.valueOf(compo.getId()), compoData);
                }

                if (!res)
                    throw new Exception("Erreur d'insertion de la composition dans la Base de données !");
            }
        }
        return true;
    }

    /**
     * Récupérer le nom d'un article
     * @param pos la position de l'article dans la liste
     * @return le nom de l'article
     */
    public String getArticleName(int pos) {
        return mArticleList.get(pos).getName();
    }

    /**
     * Récupérer le prix d'un article
     * @param pos la position de l'article dans la liste
     * @return le prix de l'article
     */
    public int getArticleCost(int pos) {
        return mArticleList.get(pos).getCost();
    }

    /**
     * Récupérer le stock d'un article
     * @param pos la position de l'article dans la liste
     * @return le stock de l'article
     */
    public int getArticleStock(int pos) {
        return mArticleList.get(pos).getStock();
    }

    /**
     * Récupérer le nombre d'articles
     * @return le nombre d'articles
     */
    public int getArticleCount(){
        return mArticleList.size();
    }

    /**
     * Récupérer le nom d'un combo
     * @param pos la position du combo dans la liste des combos
     * @return le nom du combo
     */
    public String getComboName(int pos){
        return mComboList.get(pos).getName();
    }

    /**
     * Récupérer le stock d'un combo
     * @param pos la position du combo dans la liste des combos
     * @return le stock du combo
     */
    public int getComboStock(int pos){
        return mComboList.get(pos).getStock();
    }

    /**
     * Récupérer le prix d'un combo
     * @param pos la position du combo dans la liste des combos
     * @return le prix du combo
     */
    public int getComboCost(int pos){
        return mComboList.get(pos).getCost();
    }

    /**
     * Récupérer le nombre de combos
     * @return le nombre
     */
    public int getComboCount(){
        return mComboList.size();
    }

    /**
     * Récupérer la composition d'un combo
     * @param pos la position du combo dans la liste
     * @return la composition
     */
    public Map<String, Integer> getComboComposition(int pos){
        Map<Article, Integer> compositionTable = mComboList.get(pos).getComPositionTable();
        Map<String, Integer> comboComposition = new Hashtable<>();

        // ajouter le nom d'un article ainsi que sa quantité dans la composition de l'article
        for (Map.Entry<Article, Integer> e: compositionTable.entrySet()) {
            comboComposition.put(e.getKey().getName(), e.getValue());
        }
        return comboComposition;
    }

    /**
     * Abandonner l'opération
     */
    public void cancel(){
        getAllBuyables();
    }

}

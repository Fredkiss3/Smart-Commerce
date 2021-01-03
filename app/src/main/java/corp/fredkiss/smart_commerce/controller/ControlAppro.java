package corp.fredkiss.smart_commerce.controller;

import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import corp.fredkiss.smart_commerce.model.Article;
import corp.fredkiss.smart_commerce.model.InfoAppro;
import corp.fredkiss.smart_commerce.model.MyDBHelper;
import corp.fredkiss.smart_commerce.model.PanierAchats;

/**
 * Contrôleur qui gère l'approvisionnement
 */
public final class ControlAppro {
    private static ControlAppro ourInstance = null;
    private final MyDBHelper mDBHelper;

    // Liste des articles enregistrés dans la BDD
    private List<Article> mArticleList;

    // Liste des articles présents dans le panier d'achats
    private PanierAchats mPanierAchats;

    private int mCreatedCount = 0;

    /**
     * Table contenant les références des articles de la liste d'articles (mArticleList)  : <br>
     *
     * key   : référence de l'article <br>
     * value : position de l'article dans la liste d'articles (mArticleList)  <br>
     */
    private Map<String, Integer> mRefArticleMap = new HashMap<>();

    /**
     * Constructeur
     * @param contexte le contexte
     */
    private ControlAppro(Context contexte) {
        mDBHelper = new MyDBHelper(contexte);
        getAllArticles();
    }

    /**
     * Récupérer tous les articles enregistrés dans la BDD
     */
    private void getAllArticles() {
        Cursor cur = mDBHelper.getAllData("Article", "nomProd");
        mPanierAchats = new PanierAchats();
        mArticleList = new ArrayList<>();

        while (cur.moveToNext()){
            // parcourir le curseur et ajouter des articles à la liste
            Article article = new Article(cur.getString(0), capitalizeWord(cur.getString(1)), 0, cur.getInt(3));
            article.setPrixU(cur.getInt(2));
            mArticleList.add(article);
        }

        // Récupérer toute la liste des id
        cur = mDBHelper.getAllData("PanierAchats", "id");

        // l'id
        int id = 0;

        // tant qu'il y a des éléments l'id sera le suivant
        while (cur.moveToNext()) {
            id = cur.getInt(0);
        }

        // Affecter le prochain id au panier
        mPanierAchats.setIdPanier(++id);

    }

    /**
     * @return l'instance
     */
    public static ControlAppro getInstance(Context contexte) {
        if (ControlAppro.ourInstance == null) {
            ourInstance = new ControlAppro(contexte);
        }
        return ourInstance;
    }

    /**
     * Ajouter un nouvel article dans le panier d'achats
     * @param nom le nom de l'article
     * @param prixAchat le prix unitaire
     * @param stock le stock
     * @param prixGlobal le prix Global de l'article
     */
    public boolean createArticle(String nom, int prixAchat, int stock, int prixGlobal) {

        String ref;
        boolean isPresent;

        // trouver une référence unique qui n'est pas dans la liste des
        // références
        do {
            isPresent = false;

            // générer une nouvelle référence
            ref = Article.generateRef();

            // vérifier que la référence de l'article n'existe pas déjà dans la liste
            for (Article article: mArticleList) {
                if(ref.equals(article.getRef())) {
                    isPresent = true;
                    break;
                }
            }

        } while (isPresent);


        // créer un nouvel article et l'ajouter à la liste et au panier
        Article article = new Article(ref, nom, prixAchat, stock);
        article.setCreated(true);
        mArticleList.add(article);

        // ajuster le maping
        mRefArticleMap.put(ref, mArticleList.size() - 1);

        // incrémenter le nombre d'articles créés
        mCreatedCount++;

        return mPanierAchats.addArticle(article, stock, prixGlobal);
    }

    /**
     * Ajouter un ancien article dans le panier d'achats
     * @param pos position de l'article dans la liste des anciens articles
     * @param prixAchat le prix unitaire
     * @param nbAchetes le stock
     * @param prixGlobal le prix Global de l'article
     * @return -> 'vrai' si l'article n'est pas présent <br>
     *     -> 'faux' dans le cas contraire
     */
    public boolean addArticle(int pos, int prixAchat, int nbAchetes, int prixGlobal) {

        // récupérer l'article et l'ajouter au panier
        Article article = mArticleList.get(pos);

        // modifier le prix unitaire et le stock
        article.setPrixAchat(prixAchat);
        article.setNbAchetes(nbAchetes);

        // ajuster le maping
        mRefArticleMap.put(article.getRef(), pos);

        // ajouter l'article au panier
        return mPanierAchats.addArticle(article, nbAchetes, prixGlobal);
    }

    /**
     * Retirer un article du panier d'achats
     * @param pos la position de l'article dans le panier d'achats
     * @return - 'false' si l'article n'existe pas dans la liste <br>
     *         - 'true' dans le cas contraire
     */
    public boolean removeArticle(int pos) {
        boolean removed = false;

        String ref = mPanierAchats.getArticleRef(pos);

        // vérifier que la référence de l'article existe pas déjà dans la liste
        Article toDelete = mArticleList.get(mRefArticleMap.get(ref));

        if(toDelete != null) {

            if(mPanierAchats.removeArticle(ref)) {
                removed = true;
            }

            // enlever l'article de la liste d'articles (mArticleList)  si celui-ci vient d'être créé
            if(toDelete.isCreated()) {
                mArticleList.remove(toDelete);
                mCreatedCount--;
            }
        }

        // ajuster le mapping
        mRefArticleMap.remove(ref);

        return removed;
    }

    /**
     * Modifier les informations d'un article
     * @param pos la position de l'article dans le panier d'achats
     * @param nom son nom
     * @param prixAchat son prix Unitaire
     * @param stock son stock
     * @param prixGlobal son prix Global
     * @return - 'false' si l'article n'existe pas <br>
     *         - 'vrai' si l'article a été modifié <br>
     *         - Si l'article n'est pas complètement modifiable, son nom ne pourra pas être modifié
     */
    public boolean modifyArticle(int pos, String nom,  int prixAchat, int stock, int prixGlobal) {
        boolean modified = false;

        String ref = mPanierAchats.getArticleRef(pos);

        // vérifier que la référence de l'article existe pas déjà dans la liste
        Article toModify = mArticleList.get(mRefArticleMap.get(ref));

        if(toModify != null) {
            // modifier le prix unitaire et le stock
            toModify.setPrixAchat(prixAchat);
            toModify.setNbAchetes(stock);

            if(toModify.isCreated()) {
                // modifier le nom
                toModify.setNomProd(nom);
            }

            // signaler que l'article a été modifié
            modified = mPanierAchats.updateArticle(pos, nom, prixAchat, stock, prixGlobal);
        }

        return modified;
    }

    /**
     * Ajouter les dépenses supplémentairess
     * @param depSup dépenses
     */
    public void addDepSup(int depSup) {
        mPanierAchats.setDepSup(depSup);
    }

    /**
     * Enregistrer les modifications dans la BDD
     */
    public void save() throws Exception {
        boolean res;
        for (Article article: mArticleList) {
            Map<String, String> articleData  = article.getAllData();

            // ajouter un article créé
            if (article.isCreated()) {
                res =  mDBHelper.insertData("Article", articleData);
                if (!res)
                    throw new Exception("Erreur d'insertion de l'article créé dans la Base de données !");

            } else {
                // modifier un article déjà existant
                res = mDBHelper.updateData("Article", article.getIdName(), article.getRef(), articleData);
                if (!res)
                    throw new Exception("Erreur de modification de l'article dans la Base de données !");
            }
        }

        // ajouter le panier
        Map<String, String> panierData = mPanierAchats.getAllData();
        res = mDBHelper.insertData("PanierAchats", panierData);
        if (!res)
            throw new Exception("Erreur d'insertion du panier dans la Base de données !");

        List<InfoAppro> approList = mPanierAchats.getInfoApproList();

        // ajouter les informations du panier
        for (InfoAppro info: approList) {
            Map<String, String> infoApproData = info.getAllData();
            res = mDBHelper.insertData("InfoAppro", infoApproData);
            if (!res)
                throw new Exception("Erreur d'insertion de l'information du panier dans la Base de données !");
        }

        getAllArticles();
    }

    /**
     * Récupérer le nom d'un article dans la liste d'articles (mArticleList)
     * @param pos la position
     * @return le nom de l'article
     */
    public String getArticleName(int pos){
        return mArticleList.get(pos).getName();
    }

    /**
     * Récupérer la liste des nom des articles
     * @return la liste
     */
    public List<String> getArticleNameList() {
        List<String> articleList = new ArrayList<>();

        for (Article a:mArticleList) {
            articleList.add(a.getName());
        }

        return articleList;
    }

    /**
     * Récupérer le Taille de la liste du panier d'achats
     * @return la taille
     */
    public int getBasketListCount() {
        return mPanierAchats.getCount();
    }

    /**
     * Récupérer le prix global d'un article dans le panier d'achats
     * @param pos la position de l'article
     * @return prix Global
     */
    public int getBasketArticleGCost(int pos) {
        return mPanierAchats.getArticleGCost(pos);
    }

    /**
     * Récupérer le prix unitaire d'un article dans le panier d'achats
     * @param pos la position de l'article
     * @return prix unitaire
     */
    public int getBasketArticleBuyingCost(int pos) {
        return mPanierAchats.getArticleBuyingCost(pos);
    }

    /**
     * Récupérer le prix unitaire d'un article dans la liste d'articles (mArticleList)
     * @param pos la position de l'article
     * @return prix unitaire
     */
    public int getArticleBuyingCost(int pos){
        return mArticleList.get(pos).getPrixAchat();
    }

    /**
     * Récupérer le nombre d'unités achetés d'un article dans le panier d'achats
     * @param pos la position de l'article
     * @return nombre d'unités
     */
    public int getBasketArticleNbBought(int pos) {
        return mPanierAchats.getArticleNbBought(pos);
    }

    /**
     * Récupérer le nom d'un article dans le panier d'achats
     * @param pos la position de l'article
     * @return nom
     */
    public String getBasketArticleName(int pos) {
        return mPanierAchats.getArticleName(pos);
    }

    /**
     * Id du panier
     * @return id
     */
    public int getIdPanier() {
        return mPanierAchats.getIdPanier();
    }

    /**
     * Date du panier
     * @return date au format jj - mm - aaaa
     */
    public String getDatePanier() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy", Locale.US);
        return dateFormat.format(mPanierAchats.getDate());
    }

    /**
     * Le coût total du panier
     * @return coût total
     */
    public int getTotalCost(){
        return mPanierAchats.getTotalCost();
    }

    /**
     * Abandonner l'opération
     */
    public void cancel() {
        getAllArticles();
    }

    /**
     * Récupérer les dépenses supplémentaires
     * @return les dépenses sup
     */
    public String getDepSup() {
        return "" + mPanierAchats.getDepSup();
    }

    /**
     * Pour savoir si un article est créé
     * @param pos la position de l'article dans la liste d'articles (mArticleList)
     * @return - 'true' ou 'false'
     */
    public boolean isCreated(int pos) {
        return mPanierAchats.isCreated(pos);

    }

    /**
     * Mettre la première lettre d'un mot en majuscule
     * @param word le mot
     * @return le mot modifié
     */
    private String capitalizeWord(String word) {
        char first = word.charAt(0);
        return word.replaceFirst(Character.toString(first), Character.toString(first).toUpperCase());
    }

    /**
     * Récupérer la taille de l'ensemble des éléments créés
     * @return la taille
     */
    public int getCreatedCount() {
        return mCreatedCount;
    }
}

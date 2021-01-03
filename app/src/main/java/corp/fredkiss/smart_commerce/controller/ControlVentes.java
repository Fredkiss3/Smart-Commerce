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
import corp.fredkiss.smart_commerce.model.FicheVente;
import corp.fredkiss.smart_commerce.model.IncorrectCompositionException;
import corp.fredkiss.smart_commerce.model.Journal;
import corp.fredkiss.smart_commerce.model.MyDBHelper;
import corp.fredkiss.smart_commerce.model.NotAvailableException;
import corp.fredkiss.smart_commerce.model.NotEnoughStockException;

public final class ControlVentes  {

    private static ControlVentes ourInstance = null;
    private final MyDBHelper mDBHelper;
    private List<String> mBuyableNamesList;
    private Journal mJournal;

    // éléments à vendre
    private List<Buyable> mBuyables;

    /**
     * Constructeur
     * @param contexte le contexte
     */
    private ControlVentes(Context contexte) {
        mDBHelper = new MyDBHelper(contexte);
        getAllBuyables();
    }

    /**
     * @return l'instance
     */
    public static ControlVentes getInstance(Context contexte) {
        if (ControlVentes.ourInstance == null) {
            ourInstance = new ControlVentes(contexte);
        }
        return ourInstance;
    }

    /**
     * Récupérer tous les éléments de la base de données
     */
    private void getAllBuyables() {
        Map<String, Buyable> buyableMap = new Hashtable<>();
        mBuyables = new ArrayList<>();
        mBuyableNamesList = new ArrayList<>();

        // Récupérer l'ensemble des articles
        Cursor cur = mDBHelper.getAllData("Article", "nomProd");

        while (cur.moveToNext()) {
            // parcourir le curseur et ajouter des articles à la liste
            Article article = new Article(cur.getString(0), cur.getString(1), 0, cur.getInt(3));
            article.setPrixU(cur.getInt(2));

            if(article.getStock() > 0) {
                // N'ajouter l'article que lorsque le stock est supérieur à zéro
                mBuyables.add(article);
                mBuyableNamesList.add(article.getName());
            }

            // ajouter un article à la Map
            buyableMap.put(article.getRef(), article);
        }

        // Récupérer l'ensemble des combos
        cur = mDBHelper.getAllData("Combo", "nomCombo");

        while (cur.moveToNext()) {
            // parcourir le curseur et ajouter des combos à la liste
            Combo combo = new Combo(cur.getString(0), cur.getString(1));

            // ajouter un combo à la Map
            buyableMap.put(combo.getRef(), combo);
        }

        // récupérer les tables de compositions
        Cursor cur_2 = mDBHelper.getAllData("Composition", "id");

        while (cur_2.moveToNext()) {

            String refArticle = cur_2.getString(1);
            String refCombo = cur_2.getString(2);
            int qtite = cur_2.getInt(3);

            // Récupérer l'article et le combo
            Article a = (Article) buyableMap.get(refArticle);
            Combo c = (Combo) buyableMap.get(refCombo);

            try {
                c.addArticle(a, qtite);
            } catch (IncorrectCompositionException e) {
                e.printStackTrace();
            }

            if (c.isAvailable()) {
                // Ajouter le combo à la liste si et seulement si le stock est positif
                mBuyables.add(c);
                mBuyableNamesList.add(c.getName());
            }

        }

        // Le mJournal d'aujourd'hui
        mJournal = new Journal();

        // Récupérer les articles vendus ce jour-ci
        cur = mDBHelper.execSQLReturn("SELECT * FROM FicheVenteArticle WHERE dateJournal = ?", new String[]{
                mJournal.getDateToString()
        });

        while (cur.moveToNext()) {
            String ref = cur.getString(2);
            int nbBought = cur.getInt(3);
            int nbLost = cur.getInt(4);
            int prixUnitaire = cur.getInt(5);

            // L'article
            Article article = (Article) (buyableMap.get(ref));

            // ajouter une vente
            mJournal.addVente(article, nbBought, nbLost, prixUnitaire);
        }

        // Récupérer les combos vendus ce jour-ci
        cur = mDBHelper.execSQLReturn("SELECT * FROM FicheVenteCombo WHERE dateJournal = ?", new String[]{
                mJournal.getDateToString()
        });

        while (cur.moveToNext()) {
            String ref = cur.getString(2);
            int nbBought = cur.getInt(3);
            int nbLost = cur.getInt(4);
            int prixUnitaire = cur.getInt(5);

            // L'article
            Combo combo = (Combo) (buyableMap.get(ref));

            // ajouter une vente
            mJournal.addVente(combo, nbBought, nbLost, prixUnitaire);
        }
    }

    /**
     * Récupérer le nom d'un élément vendu
     * @param pos la position de l'élément dans le journal
     * @return le nom
     */
    public String getJournalBuyableName(int pos) {
        return mJournal.getBuyableName(pos);
    }

    /**
     * Récupérer le prix de l'élément vendu
     * @param pos la position de l'élément dans le journal
     * @return le prix
     */
    public int getJournalBuyablePrixUnitaire(int pos) {
        return mJournal.getBuyablePrixUnitaire(pos);
    }

    /**
     * Récupérer la somme totale perdue pour un élément vendu
     * @param pos la position de l'article dans le journal
     * @return la somme
     */
    public int getJournalBuyableTotalLost(int pos) {
        return mJournal.getBuyableTotalLost(pos);
    }

    /**
     * Récupérer la somme totale perdue pour un élément vendu
     * @param pos la position de cet élément dans le journal
     * @return la somme
     */
    public int getJournalBuyableTotalBought(int pos) {
        return mJournal.getBuyableTotalBought(pos);
    }

    /**
     * Récupérer le nombre d'exemplaires vendus pour un élément
     * @param pos la position de cet élément dans le journal
     * @return le nombre vendu
     */
    public int getJournalBuyableNbBought(int pos) {
        return mJournal.getBuyableNbBought(pos);
    }

    /**
     * Récupérer le nombre d'exemplaires perdus de l'élément
     * @param pos la position de cet élément dans le journal
     * @return le nombre perdu
     */
    public int getJournalBuyableNbLost(int pos) {
        return mJournal.getBuyableNbLost(pos);
    }

    /**
     * Récupérer la date du journal
     * @return la date en français
     */
    public String getDateJournal() {
        return mJournal.getDate();
    }

    /**
     * Nombre d'éléments du journal
     * @return le nombre d'éléments
     */
    public int getCount() {
        return mJournal.getCount();
    }

    /**
     * Vendre un élément
     * @param pos la position de l'élément dans la liste des éléments pouvant être vendus
     * @param nbBought le nombre d'élements vendus
     * @param nbLost le nombre d'éléments perdus
     */
    public boolean sell(int pos, int nbBought, int nbLost) {

        Buyable b = mBuyables.get(pos);
        boolean done = false;

        try {
            // si la vente a bien eu lieu
            b.sell(nbBought, nbLost);

           // Ajouter au journal et Enregistrer dans la BDD
            FicheVente fiche = mJournal.addVente(b, nbBought, nbLost, b.getCost());
            Map<String, String> data = fiche.getAllData();

            if(fiche.isCombo()) {
                Combo c = (Combo) b;
                System.out.println(mDBHelper.insertData("FicheVenteCombo", data));
                System.out.println(mDBHelper.updateData("Combo",
                        c.getIdName(), c.getRef(), c.getAllData()));
            } else {
                Article a = (Article) b;
                System.out.println(mDBHelper.insertData("FicheVenteArticle", data));
                System.out.println(mDBHelper.updateData("Article",
                        a.getIdName(), a.getRef(), a.getAllData()));
            }

            done = true;

        } catch (NotAvailableException e) {
            e.printStackTrace();
        } catch (NotEnoughStockException e) {
            e.printStackTrace();
        }
        return done;
    }

    public List<String> getBuyableNamesList() {
        return mBuyableNamesList;
    }

    /**
     * Récupérer le prix unitaire d'un élément dans la liste des éléments
     * @param pos la position de l'élément dans cette liste
     * @return le prix unitaire
     */
    public int getPrixUnitaire(int pos) {
        return mBuyables.get(pos).getCost();
    }

    /**
     * Recharger la base de données
     */
    public void reload(){
        getAllBuyables();
    }

}

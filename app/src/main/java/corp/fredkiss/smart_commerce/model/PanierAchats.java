package corp.fredkiss.smart_commerce.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe permettant de gérer un approvisionnement
 */
public class PanierAchats implements DBInscriptable{

    private Date mDate;
    private int mDepSup;
    private int mIdPanier = 0;

    private List<InfoAppro> mInfoApproList = new ArrayList<>();

    /**
     * Constructeur
     */
    public PanierAchats() {
        // Initialiser la date et les dépenses
        this.mDate = new Date();
        this.mDepSup = 0;
    }

    public PanierAchats(Date date, int depSup) {
        // Initialiser la date et les dépenses
        this.mDate = date;
        this.mDepSup = depSup;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////    METHODES    //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Ajouter un article dans le panier d'achats
     * @param article l'article
     * @param nbBought la quantité achetée
     * @param globalCost le prix d'achat global du produit
     * @return vrai si l'élément n'existe pas déjà <br> faux, si l'élément existe
     */
    public boolean addArticle(Article article, int nbBought, int globalCost) {
        for (InfoAppro info: mInfoApproList) {
            if (info.getRefProd().equals(article.getRef())) {
                return false;
            }
        }

        if (nbBought > 0 && globalCost > 0) {
            InfoAppro infoAppro = new InfoAppro(article.getRef(),
                    globalCost, nbBought, article.getPrixAchat(), article.getName()
            );
            infoAppro.setIdPanier(getIdPanier());
            infoAppro.setCreated(article.isCreated());
            mInfoApproList.add(infoAppro);
            return true;
        }

        return false;
    }

    /**
     * Retirer un article du panier d'achats (-> Seulement en mode 'création')
     * @param ref la référence de l'article concerné
     * @return vrai si la référence a été trouvée
     */
    public boolean removeArticle(String ref){
        for (InfoAppro info: mInfoApproList) {
            if (info.getRefProd().equals(ref)) {
                mInfoApproList.remove(info);
                return true;
            }
        }

        return false;
    }

    /**
     * Mettre à jour du panier d'achats
     * @param pos la position de l'article dans le panier
     * @param newNbBought la nouvelle quantité
     * @param newGcost le nouveau prix global
     * @return vrai si la référence a été trouvée
     */
    public boolean updateArticle(int pos, String name, int newBuyingCost, int newNbBought, int newGcost) {
        InfoAppro info = mInfoApproList.get(pos);

        if(newNbBought > 0 && newGcost > 0 && name.trim().length() > 0 && newBuyingCost > 0) {
            // changer les informations de l'élément
            info.setNomProd(name);
            info.setBuyingCost(newBuyingCost);
            info.setNbAchetes(newNbBought);
            info.setPrixGlobal(newGcost);
            return true;
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////    SETTERS     //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * L'id du panier
     * @param idPanier l'id
     */
    public void setIdPanier(int idPanier) {
        mIdPanier = idPanier;
    }

    /**
     * Affecter une valeur à la dépense
     * @param depSup dépenses
     * @return vrai si cette valeur est supérieure à zéro
     */
    public boolean setDepSup(int depSup) {
        if(depSup > 0) {
            mDepSup = depSup;
            return true;
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////    GETTERS     //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * récupérer l'ensemble des informations des articles pour l'approvisionnement actuel
     */
    public List<InfoAppro> getInfoApproList() {
        return mInfoApproList;
    }

    /**
     * Récupérer la date
     * @return la date
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Les divers dépenses entrant en jeu lors de l'approvisionnement
     * @return cette somme
     */
    public int getDepSup() {
        return mDepSup;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();

        data.put("id", String.valueOf(getIdPanier()));
        data.put("dateAchat", getDateToString());
        data.put("depenseSup", String.valueOf(mDepSup));
        data.put("somDepensee", String.valueOf(getSomDep()));
        return data;
    }

    /**
     * Calculer la somme dépensée
     * @return la somme dépensée
     */
    public int getSomDep() {
        int somDep = 0;

        for (InfoAppro info: mInfoApproList){
            somDep += info.getGlobalCost();
        }

        // Ne pas oublier d'ajouter les dépenses supplémentaires
        somDep += mDepSup;

        return somDep;
    }

    /**
     * récupérer la date sous le format "AAAA-MM-JJ" afin de l'introduire dans la BDD
     * @return la date
     */
    public String getDateToString() {
        // convertir la date dans le format accepté par la BDD
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(this.mDate);
    }

    @Override
    public String getIdName() {
        return "id";
    }

    /**
     * Récupérer l'id du panier
     * @return l'id
     */
    public int getIdPanier() {
        return mIdPanier;
    }

    /**
     * Prix unitaire de l'article
     * @param pos position
     * @return le prix unitaire
     */
    public int getArticleBuyingCost(int pos){
        return mInfoApproList.get(pos).getBuyingCost();
    }

    /**
     * Nombre d'unités achetés
     * @param pos position
     * @return nombre d'unités
     */
    public int getArticleNbBought(int pos) {
        return mInfoApproList.get(pos).getNbAchetes();
    }

    /**
     * Nom de l'article
     * @param pos position
     * @return nom
     */
    public String getArticleName(int pos){
        return mInfoApproList.get(pos).getNomProd();
    }

    /**
     * Prix Global de l'article
     * @param pos position
     * @return prix Global
     */
    public int getArticleGCost(int pos){
        return mInfoApproList.get(pos).getGlobalCost();
    }

    /**
     * Référence d'un article
     * @param pos la position
     * @return référence
     */
    public String getArticleRef(int pos) {
        return mInfoApproList.get(pos).getRefProd();
    }

    /**
     * Prix total du panier
     * @return prix total
     */
    public int getTotalCost() {
        int totalCost = 0;
        for (InfoAppro info: mInfoApproList) {
            totalCost += info.getGlobalCost();
        }

        return totalCost;
    }

    public boolean isCreated(int pos) {
        return mInfoApproList.get(pos).isCreated();
    }

    /**
     * Récupérer la taille de la liste du panier
     * @return la taille
     */
    public int getCount() {
        return mInfoApproList.size();
    }
}






















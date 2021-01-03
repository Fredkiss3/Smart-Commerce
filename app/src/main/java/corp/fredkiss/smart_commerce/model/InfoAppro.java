package corp.fredkiss.smart_commerce.model;

import java.util.Hashtable;
import java.util.Map;

/**
 * Classe contenant les informations d'un approvisionnement
 */
public class InfoAppro implements DBInscriptable {

    private String mRefProd;
    private String mNomProd;
    private int mNbAchetes;
    private int mBuyingCost;
    private int mPrixGlobal;
    private int mIdPanier;
    private boolean mCreated = false;


    /**
     * Constructeur explicite
     * @param refProd la référence du produit
     * @param prixGlobal le prix Global du produit
     * @param nbAchetes le stock
     * @param nom nom du produit
     * @param prixAchat prix unitaire du produit
     */
    InfoAppro(String refProd, int prixGlobal, int nbAchetes, int prixAchat, String nom) {
        mRefProd = refProd;
        mPrixGlobal = prixGlobal;
        mNbAchetes = nbAchetes;
        mIdPanier = 0;
        mBuyingCost = prixAchat;
        mNomProd = nom;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();

        data.put("refArticle", getRefProd());
        data.put("idPanier", String.valueOf(getIdPanier()));
        data.put("nbAchetes", String.valueOf(getNbAchetes()));
        data.put("prixGlobal", String.valueOf(getGlobalCost()));

        return data;
    }

    /**
     * Nom du produit
     * @return nom
     */
    public String getNomProd() {
        return mNomProd;
    }

    /**
     * Récupérer le prix unitaire
     * @return le prix unitaire
     */
    public int getBuyingCost() {
        return mBuyingCost;
    }

    @Override
    public String getIdName() {
        return "id";
    }

    /**
     * récupérer la référence
     * @return le référence
     */
    public String getRefProd() {
        return mRefProd;
    }

    /**
     * Récupérer le stock
     * @return le stock
     */
    public int getNbAchetes() {
        return mNbAchetes;
    }

    /**
     * Récupérer Le prix Global
     * @return prix Global
     */
    public int getGlobalCost() {
        return mPrixGlobal;
    }

    /**
     * Récupérer l'id du panier
     * @return l'id
     */
    public int getIdPanier() {
        return mIdPanier;
    }

    /**
     * affecter le nbAchetes
     * @param nbAchetes le nbAchetes
     */
    public void setNbAchetes(int nbAchetes) {
        mNbAchetes = nbAchetes;
    }

    /**
     * Affecter le prix Global
     * @param prixGlobal le prix Global
     */
    public void setPrixGlobal(int prixGlobal) {
        mPrixGlobal = prixGlobal;
    }

    /**
     * Affecter un du panier
     * @param idPanier id du panier
     */
    public void setIdPanier(int idPanier) {
        mIdPanier = idPanier;
    }

    /**
     * Pour savoir si l'on vient de créer un article
     * @return l'état de l'article
     */
    public boolean isCreated() {
        return mCreated;
    }

    /**
     * Affecter l'état 'créé' à l'article
     * @param created état
     */
    public void setCreated(boolean created) {
        mCreated = created;
    }

    public void setNomProd(String nomProd) {
        mNomProd = nomProd;
    }

    public void setBuyingCost(int buyingCost) {
        mBuyingCost = buyingCost;
    }

}

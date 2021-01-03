package corp.fredkiss.smart_commerce.model;

import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

public class Article implements DBInscriptable, Buyable {

    private String mRefProd;
    private String mNomProd;
    private int mPrixU;
    private int mStock;
    private int mPrixAchat = 0;
    private boolean mCreated;
    private int mNbAchetes = 0;

    /**
     * Constructeur explicite
     *
     * @param ref       la référence du produit
     * @param nom       le nom du produit
     * @param prixAchat le prix unitaire du produit
     * @param stock     le stock courant du produit
     */
    public Article(String ref, String nom, int prixAchat, int stock) {
        this.setRefProd(ref);
        this.setNomProd(nom);
        this.setPrixAchat(prixAchat);
        this.setStock(stock);
        mCreated = false;
    }

    /**
     * Constructeur par défaut
     */
    public Article(){
        this.mNomProd = "article";
        this.mRefProd = generateRef();
        this.mPrixU = 1;
        this.mStock = 0;
        mCreated = false;
    }

    /**
     * Générer une référence <br>
     * La référence est sous la forme : 2 lettres suivies de 2 chiffres
     * @return une nouvelle référence
     */
    public static String generateRef() {
        StringBuilder ref = new StringBuilder();
        int position;
        String chars = "WXCVAZERTYUISDFGHOPBNQJKLM";
        String numbers = "1027985346";

        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                position = (int) (Math.random() * chars.length());
                ref.append(chars.charAt(position));
            }else {
                position = (int) (Math.random() * numbers.length());
                ref.append(numbers.charAt(position));
            }
        }
        return ref.toString();
    }

    /**
     * Affecter la référence du produit
     * @param refProd la référence
     */
    private void setRefProd(String refProd) {
       if(!refProd.trim().isEmpty()) {
           mRefProd = refProd;
       }
    }

    /**
     * Donner un nom au produit
     * @param nomProd le nom
     */
    public void setNomProd(String nomProd) {
        if(!nomProd.trim().isEmpty()) {
            mNomProd = nomProd;
        }
    }

    /**
     * Afecter Le prix unitaire
     * @param prixU le prix
     * @return vrai si le prix est supérieur à zéro
     */
    public boolean setPrixU(int prixU) {
        if(prixU > 0) {
            mPrixU = prixU;
            return true;
        }
        return false;
    }

    /**
     * Affecter Le stock
     * @param stock le stock
     */
    public void setStock(int stock) {
        if(stock >= 0) mStock = stock;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();

        // Les données
        data.put("ref", getRef());
        data.put("nomProd", getName());
        data.put("prixU", String.valueOf(getCost()));
        data.put("stockCourant", String.valueOf(getStock()));


        // TODO : PRENDRE LE PRIX UNITAIRE D'ACHAT EN COMPTE
        // data.put("", String.valueOf(getPrixAchat()));
        return data;
    }

    @Override
    public String getIdName() {
        return "ref";
    }

    @Override
    public String getRef() {
        return mRefProd;
    }

    @Override
    public int getCost() {
        return mPrixU;
    }


    @Override
    public String getName() {
        return mNomProd;
    }


    public int getStock() {
        return mStock + mNbAchetes;
    }


    @Override
    public int sell(int nbBought, int nbLost) throws NotAvailableException, NotEnoughStockException {

        if (mStock > 0) {
            if ((nbBought + nbLost) <= mStock) {
                    int sold =  mPrixU * nbBought;

                    mStock -= nbBought + nbLost;
                    return sold;
            } else {
                // Gérer l'erreur de stock insuffisant
                throw new NotEnoughStockException("le stock est insuffisant.");
            }
        } else {
            // si le stock du produit est nul
            throw new NotAvailableException("Le stock de l'article est nul");
        }
    }

    /**
     * Pour savoir si l'on a le droit de complètement modifier l'article
     * @return vrai si oui
     */
    public boolean isCreated() {
        return mCreated;
    }

    /**
     * Rendre l'article totalement modifiable ou non
     * @param created vrai ou faux
     */
    public void setCreated(boolean created) {
        mCreated = created;
    }

    /**
     * affecter le nombre d'unités achetés
     * @param nbAchetes le nombre d'unités
     */
    public void setNbAchetes(int nbAchetes) {
        if(nbAchetes > 0) mNbAchetes = nbAchetes;
    }

    /**
     * récupérer le nombre d'unités achetés
     */
    public int getNbAchetes() {
        return mNbAchetes;
    }

    /**
     * Affecter le prix unitaire d'achat
     * @param prixAchat le prix donné
     */
    public void setPrixAchat(int prixAchat) {
        if(prixAchat > 0)
            mPrixAchat = prixAchat;
    }

    /**
     * Récupérer le prix d'achats
     * @return le prix unitaire d'achat
     */
    public int getPrixAchat() {
        return mPrixAchat;
    }
}

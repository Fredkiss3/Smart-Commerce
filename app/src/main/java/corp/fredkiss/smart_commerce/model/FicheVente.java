package corp.fredkiss.smart_commerce.model;

import java.util.Hashtable;
import java.util.Map;

public class FicheVente implements DBInscriptable{
    private Buyable buyable;
    private int nbVendus;
    private int nbPerdus;
    private String mDateJournal;
    private int prixUnitaire;

    /**
     * Constructeur
     * @param b l'élément à vendre
     * @param nbLost nombre perdus
     * @param nbBought nombre vendus
     * @param dateJournal  date du journal
     */
    FicheVente(Buyable b, int nbBought,  int nbLost, String dateJournal, int prixVente) {
        this.buyable = b;
        this.nbVendus = nbBought;
        this.nbPerdus = nbLost;
        this.mDateJournal = dateJournal;
        this.prixUnitaire = prixVente;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();

        // date du journal
        data.put("dateJournal", getmDateJournal());

        // La référence
        if(buyable.getClass() != Combo.class) {
            data.put("refArticle", getBuyable().getRef());
        } else {
            data.put("refCombo", getBuyable().getRef());
        }

        // quantités vendues, perdues & prix
        data.put("qtiteVendus", String.valueOf(getNbVendus()));
        data.put("qtitePerdus", String.valueOf(getNbPerdus()));
        data.put("prixUnitaire", String.valueOf(prixUnitaire));

        // total ventes et total pertes
        data.put("montantVentes", String.valueOf(getTotalVentes()));
        data.put("montantPertes", String.valueOf(getTotalPertes()));

        // Les données
        return data;
    }

    @Override
    public String getIdName() {
        return "id";
    }

    public int getTotalVentes() {
        return prixUnitaire * nbVendus;
    }

    public int getTotalPertes() {
        return prixUnitaire * nbPerdus;
    }

    public Buyable getBuyable() {
        return buyable;
    }

    public String getBuyableName() {
        return buyable.getName();
    }

    public int getNbVendus() {
        return nbVendus;
    }

    public int getNbPerdus() {
        return nbPerdus;
    }

    public String getmDateJournal() {
        return mDateJournal;
    }

    /**
     * Pour savoir si l'élément vendu est un combo ou un article
     * @return vrai ou faux
     */
    public boolean isCombo() {
        return (buyable.getClass() == Combo.class);
    }

    public int getPrixUnitaire() {
        return prixUnitaire;
    }
}

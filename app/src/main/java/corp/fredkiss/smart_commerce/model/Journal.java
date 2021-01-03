package corp.fredkiss.smart_commerce.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *  Classe permettant de présenter une vue claire des activités
 *  journalières.
 */
public class Journal implements DBInscriptable {
    private Date mDate;
    private List<FicheVente> mVentesList = new ArrayList<>();

    public Journal() {
        // Initialiser la date et les dépenses
        this.mDate = new Date();
    }

    public Journal(Date date) {
        // Initialiser la date et les dépenses
        this.mDate = date;
    }

    /**
     * Calculer la somme de l'ensemble des pertes engendrées
     * @return la somme des pertes
     */
    public int getTotalLost() {
        int totalLost = 0;

        for (FicheVente v: mVentesList) {
            totalLost += v.getTotalPertes();
        }

        return totalLost;
    }

    /**
     * Calculer la somme de l'ensemble des gains engendrées
     * @return la somme des gains (ventes)
     */
    public int getTotalBought() {
        int totalBouhght = 0;

        for (FicheVente v: mVentesList) {
            totalBouhght += v.getTotalVentes();
        }

        return totalBouhght;
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

    /**
     * Ajouter une vente
     * @param b l'élément vendu
     * @param nbBought le nombre vendus
     * @param nbLost le nombre perdus
     */
    public FicheVente addVente(Buyable b, int nbBought, int nbLost, int prixVente) {
        FicheVente fiche = new FicheVente(b, nbBought, nbLost, getDateToString(), prixVente);
        mVentesList.add(fiche);
        return fiche;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();

        data.put("dateJournal", getDateToString());
        data.put("totalPertes", String.valueOf(getTotalLost()));
        data.put("totalVentes", String.valueOf(getTotalBought()));

        return data;
    }

    @Override
    public String getIdName() {
        return "id";
    }

    /**
     * Récupérer le nom de l'élément vendu
     * @param pos la position de l'élément
     * @return le nom
     */
    public String getBuyableName(int pos) {
        return mVentesList.get(pos).getBuyableName();
    }

    /**
     * Récupérer le prix de l'élément vendu
     * @param pos la position de l'élément
     * @return le prix
     */
    public int getBuyablePrixUnitaire(int pos) {
        return mVentesList.get(pos).getPrixUnitaire();
    }

    public int getBuyableTotalLost(int pos) {
        return mVentesList.get(pos).getTotalPertes();
    }

    public int getBuyableTotalBought(int pos) {
        return mVentesList.get(pos).getTotalVentes();
    }

    /**
     * Récupérer le nombre d'exemplaires vendus pour un élément
     * @param pos la position
     * @return le nombre vendu
     */
    public int getBuyableNbBought(int pos) {
        return mVentesList.get(pos).getNbVendus();
    }

    /**
     * Récupérer le nombre d'exemplaires perdus de l'élément
     * @param pos la position
     * @return le nombre perdu
     */
    public int getBuyableNbLost(int pos) {
        return mVentesList.get(pos).getNbPerdus();
    }

    /**
     * Récupérer la date du journal
     * @return la date formattée en français
     */
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);
        return dateFormat.format(this.mDate);
    }

    /**
     * Compter le nombre d'éléments vendus
     * @return le nombre d'éléments
     */
    public int getCount() {
        return mVentesList.size();
    }

    /**
     * Récupérer la liste des ventes
     * @return liste des ventes
     */
    public List<FicheVente> getVentesList() {
        return mVentesList;
    }
}

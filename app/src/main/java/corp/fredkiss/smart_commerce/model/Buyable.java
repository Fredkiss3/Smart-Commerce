package corp.fredkiss.smart_commerce.model;


/**
 *  Interface décrivant le comportement des éléments qui peuvent se vendre
 */
public interface Buyable {
    /**
     * connaître le stock restant
     * @return le stock
     */
    int getStock();

    /**
     * Récupérer la référence de l'élément
     * @return la référence
     */
    String getRef();

    /**
     * Connaître le prix de l'élément
     * @return le prix
     */
    int getCost();

    /**
     * Vendre l'élément
     * @param nbBought nombre d'éléments vendus ?
     * @param nbLost nombre d'éléments perdus
     * @return  le coût de la vente
     * @throws NotAvailableException Si Le stock de cet élément est nul
     * @throws NotEnoughStockException si l'on essaie de vendre plus qu'il y en a
     */
    int sell(int nbBought, int nbLost) throws NotAvailableException, NotEnoughStockException;

    /**
     * récupérer le nom de l'élément
     * @return le nom
     */
    String getName();

}

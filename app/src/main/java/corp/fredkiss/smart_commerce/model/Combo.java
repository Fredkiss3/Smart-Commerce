package corp.fredkiss.smart_commerce.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Combo implements DBInscriptable, Buyable{
    private String mRefCombo;
    private String mNom;
    private Map<Article, Integer> mComPositionTable = new Hashtable<>();
    private Map<String, Article> mReferencesTable = new Hashtable<>();
    private List<Composition> mCompositionList = new ArrayList<>();
    private boolean New;


    /**
     * Constructeur
     * @param refCombo la référence du combo
     * @param nom le nom du combo
     */
    public Combo(String refCombo, String nom) {
        this.setRefCombo(refCombo);
        this.setNom(nom);
        this.New = false;
    }

    /**
     * Constructeur par défaut
     */
    public Combo() {
        this.setRefCombo(generateRef());
        this.setNom("Combo");
        this.New = false;
    }

    /**
     * Ajouter un article dans la composition
     * @param article l'article
     * @param qtite la quantité
     * @return vrai si l'élément n'existe pas déjà <br> faux, si l'élément existe pas
     * @throws IncorrectCompositionException lorsque la quantité entrant en composition est inférieure ou égale à zéro
     */
    public boolean addArticle(Article article, int qtite, int id, boolean isNew) throws IncorrectCompositionException {
        if (qtite > 0) {
            if (this.mComPositionTable.get(article) == null) {
                this.mComPositionTable.put(article, qtite);
                this.mReferencesTable.put(article.getRef(), article);

                Composition c = new Composition(getRef(), article.getRef(), qtite);
                c.setNew(isNew);
                c.setId(id);

                this.mCompositionList.add(c);
                return true;
            } else {
                // ne pas ajouter
                return false;
            }
        } else {
            throw new IncorrectCompositionException("La quantité entrant en composition " +
                    "doit être supérieure à zéro");
        }
    }

    /**
     * Ajouter un article dans la composition
     * @param article l'article
     * @param qtite la quantité
     * @return vrai si l'élément n'existe pas déjà <br> faux, si l'élément existe pas
     * @throws IncorrectCompositionException lorsque la quantité entrant en composition est inférieure ou égale à zéro
     */
    public boolean addArticle(Article article, int qtite) throws IncorrectCompositionException {
        if (qtite > 0) {
            if (this.mComPositionTable.get(article) == null) {
                this.mComPositionTable.put(article, qtite);
                this.mReferencesTable.put(article.getRef(), article);
                this.mCompositionList.add(new Composition(getRef(), article.getRef(), qtite));
                return true;
            } else {
                // ne pas ajouter
                return false;
            }
        } else {
            throw new IncorrectCompositionException("La quantité entrant en composition " +
                    "doit être supérieure à zéro");
        }
    }


    /**
     * Retirer un article de la composition (-> Seulement en mode 'création')
     * @param ref la référence de l'article concerné
     * @return vrai si la référence a été trouvée
     */
    public boolean removeArticle(String ref){
        Article article = mReferencesTable.get(ref);
        if(article != null) {
            // retirer la composition de la liste des compositions
            for (Composition c: mCompositionList) {
                if(c.getRefCombo().equals(ref)) {
                    mCompositionList.remove(c);
                    break;
                }
            }

            // enlever l'article de la table de composition et de la table de référence
            this.mComPositionTable.remove(article);
            this.mReferencesTable.remove(ref);

            return true;
        }

        return false;
    }

    /**
     * Mettre à jour la composition
     * @param ref la référence du produit
     * @param newQtite la nouvelle quantité
     * @return vrai si la référence a été trouvée
     */
    public boolean updateArticle(String ref, int newQtite){
        Article article = mReferencesTable.get(ref);

        if(article != null && newQtite > 0) {
            for (Composition c: mCompositionList) {
                if(c.getRefCombo().equals(ref)) {
                    mCompositionList.remove(c);

                    // Modifier les attributs
                    c.setRefCombo(getRef());
                    c.setRefArticle(article.getRef());
                    c.setQtiteProd(newQtite);

                    mCompositionList.add(c);
                    break;
                }
            }

            this.mComPositionTable.put(article, newQtite);
            return true;
        }

        return false;
    }


    @Override
    public int sell(int nbBought, int nbLost) throws NotAvailableException, NotEnoughStockException {
        // si le stock est supérieur à zéro
        int stock = getStock();
        if(stock > 0) {
            if ((nbBought + nbLost) <= stock) {
                    int sold = 0;

                    for (Map.Entry<Article, Integer> e : getComPositionTable().entrySet()) {
                        // récupérer l'article et sa composition
                        Article article = e.getKey();
                        int composition = e.getValue();

                        // vendre
                        sold += article.sell(nbBought * composition, nbLost * composition);
                    }
                    return sold;

            } else {
                // Gérer l'erreur de stock insuffisant
                throw new NotEnoughStockException("Le stock est insuffisant");
            }

        } else {
            // si le stock du produit est nul
            throw new NotAvailableException("le pack ne peut être vendu car le stock est nul");
        }
    }

    /**
     * récupérer le nombre d'articles contenu dans le combo
     * @return le nombre d'articles
     */
    public int count(){
        return mComPositionTable.size();
    }

    /**
     * Générer une référence <br>
     * La référence est sous la forme : 2 lettres suivies de 2 chiffres
     * @return une nouvelle référence
     */
    public static String generateRef() {
        StringBuilder ref = new StringBuilder();
        int position;
        String chars = "WXCVBNAZERTYUIOPQSDFGHJKLM";
        String numbers = "7891234560";

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


    //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////    SETTERS     //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Affecter la référence
     * @param refCombo la référence du combo
     */
    public void setRefCombo(String refCombo) {
        this.mRefCombo = refCombo;
    }

    /**
     * Affecter le nom
     * @param nom le nom
     */
    public void setNom(String nom) {
        mNom = nom;
    }

    /**
     * Affecter l'état de nouveau
     * @param aNew l'état
     */
    public void setNew(boolean aNew) {
        New = aNew;
    }

    /**
     * Récupérer la liste des compositions
     * @return la liste des compositions
     */
    public List<Composition> getCompositionList() {
        return mCompositionList;
    }

    @Override
    public int getStock(){
        int minStock = 0;
        boolean first = true;

        for (Map.Entry<Article, Integer> e : getComPositionTable().entrySet()){
            int stockArticle = e.getKey().getStock() / e.getValue();

            // Pour la 1ère fois, donner le minimum au 1er article que l'on croise
            if(first) {
                minStock = stockArticle;
                first = false;
            }

            // Chercher le minimum
            else if(stockArticle  < minStock) {
                minStock = stockArticle;
            }
        }

        return minStock;
    }

    /**
     * Récupérer un article
     * @param ref la référence
     * @return l'article concerné
     */
    public Article getArticle(String ref) throws NullPointerException {
        Article concerned = mReferencesTable.get(ref);
        if (concerned == null) throw new NullPointerException("L'article ne compose pas le produit");
        return concerned;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String> data = new Hashtable<>();
        data.put("ref", getRef());
        data.put("nomCombo", getName());
        return data;
    }

    @Override
    public String getIdName() {
        return "ref";
    }

    /**
     * Récupérer la table de composition
     * @return la table de composition
     */
    public Map<Article, Integer> getComPositionTable() {
        return mComPositionTable;
    }

    @Override
    public String getRef() {
        return mRefCombo;
    }

    /**
     * Calculer le coût
     * @return le coût
     */
    @Override
    public int getCost() {
        int cost = 0;

        for (Map.Entry<Article, Integer> e : getComPositionTable().entrySet()) {
                cost += e.getKey().getCost() * e.getValue();
        }
        return cost;
    }

    /**
     * Récupérer le nom du combo
     * @return le combo
     */
    @Override
    public String getName() {
        return mNom;
    }

    /**
     * S'il s'agit d'un nouveau combo
     * @return vrai ou faux
     */
    public boolean isNew() {
        return New;
    }


    /**
     * Pour savoir si le combo est accesible
     * @return vrai si le stock du comob est > 0 <br> faux pour le contraire
     */
    public boolean isAvailable(){
        return getStock() > 0;
    }
}

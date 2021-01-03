package corp.fredkiss.smart_commerce.model;

import java.util.Hashtable;
import java.util.Map;

/**
 * Classe permettant de stocker les informations d'une composition (Combo-article)
 */
public class Composition implements DBInscriptable {

    private String mRefCombo;
    private String mRefArticle;
    private int qtiteProd;
    private int id;
    private boolean New;

    /**
     * Contructeur explicite
     * @param refCombo la référence du combo
     * @param refArticle la référence de l'article
     * @param qtiteProd la quantité de l'article
     */
    public Composition(String refCombo, String refArticle, int qtiteProd) {
        this.mRefCombo = refCombo;
        this.mRefArticle = refArticle;
        this.qtiteProd = qtiteProd;
        this.New = false;
        this.id = 0;
    }

    @Override
    public Map<String, String> getAllData() {
        Map<String, String>  data = new Hashtable<>();

        data.put("id", String.valueOf(getId()));
        data.put("refArticle", getRefArticle());
        data.put("refCombo", getRefCombo());
        data.put("qtiteProd", String.valueOf(getQtiteProd()));
        return data;
    }

    @Override
    public String getIdName() {
        return "id";
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////        GETTERS & SETTERS        /////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////


    public String getRefCombo() {
        return mRefCombo;
    }

    public void setRefCombo(String refCombo) {
        mRefCombo = refCombo;
    }

    public String getRefArticle() {
        return mRefArticle;
    }

    public void setRefArticle(String refArticle) {
        mRefArticle = refArticle;
    }

    public int getQtiteProd() {
        return qtiteProd;
    }

    public void setQtiteProd(int qtiteProd) {
        this.qtiteProd = qtiteProd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNew() {
        return New;
    }

    public void setNew(boolean aNew) {
        New = aNew;
    }

}

package corp.fredkiss.smart_commerce.model;

import java.util.Hashtable;
import java.util.Map;

public class Crediteur extends Client implements DBInscriptable {
    private Integer idCred = 0;
    private Integer somDiff;

    public int getIdCred() {
        return idCred;
    }

    public void setIdCred(int idCred) {
        this.idCred = idCred;
    }

    public int getSomDiff() {
        return somDiff;
    }

    public Crediteur(String nomClient, String chbreClient, int somDiff) {
        super(nomClient, chbreClient);
        this.somDiff = somDiff;
    }

    @Override
    public Map<String, String> getAllData(){
        Map<String, String> data = new Hashtable<>();

        data.put("somDifference", somDiff.toString());
        data.put("nomCrediteur", getNomClient());
        data.put("chbreCrediteur", getChbreClient());
        return data;
    }

    /**
     *
     * @return le nom de la clé primaire
     */
    @Override
    public String getIdName(){
        return "id";
    }
}

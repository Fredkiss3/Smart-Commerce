package corp.fredkiss.smart_commerce.controller;

import android.content.Context;

import java.util.List;

import corp.fredkiss.smart_commerce.model.MyDBHelper;
import corp.fredkiss.smart_commerce.model.PanierAchats;

/**
 * Classe qui gère l'historique des approvisionnements
 */
public final class ControlHistoriqueAppro {
    private static ControlHistoriqueAppro ourInstance = null;
    private final MyDBHelper mDBHelper;
    private List<PanierAchats> mAchatsList;


    private ControlHistoriqueAppro(Context ctx){
        mDBHelper = new MyDBHelper(ctx);
    }

    /**
     * @return l'instance
     */
    public static ControlHistoriqueAppro getInstance(Context ctx) {
        if ( ourInstance == null) {
            ourInstance = new ControlHistoriqueAppro(ctx);
        }
        return ourInstance;
    }

    /**
     * Récupérer la liste des approvisionnements
     * TODO
     */
    private void getAllAppro() {

    }
}

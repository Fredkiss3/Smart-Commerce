package corp.fredkiss.smart_commerce.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;



public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "app.db";

    private static final String[] TABLES =  {
            "Composition",
            "InfoAppro",
            "FicheVente",
            "FicheVenteCombo",
            "InfoEmprunt",
            "PanierAchats",
            "Journal",
            "Article",
            "Combo",
            "Debiteur",
            "Crediteur"
    };

    private static final String[] CREATE_REQ = {
            "CREATE TABLE IF NOT EXISTS Article(\n" +
            "\tref TEXT NOT NULL ,\n" +
            "\tnomProd TEXT NOT NULL,\n" +
            "\tprixU INTEGER NOT NULL,\n" +
            "\tstockCourant INTEGER NOT NULL, \n" +
            "\tPRIMARY KEY(ref)"+
            ");",


            "CREATE TABLE IF NOT EXISTS Combo(\n" +
            "ref TEXT NOT NULL,\n" +
            "nomCombo TEXT NOT NULL, " +
            "PRIMARY KEY(ref)"+
            ");",

            " CREATE TABLE IF NOT EXISTS Composition(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
            "\trefArticle TEXT,\n" +
            "\trefCombo TEXT,\n" +
            "\tqtiteProd INTEGER NOT NULL,\n" +
            "\tFOREIGN KEY(refArticle) REFERENCES Article(ref),\n" +
            "\tFOREIGN KEY(refCombo) REFERENCES Combo(ref)\n" +
            ");",

            "CREATE TABLE IF NOT EXISTS PanierAchats(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tdateAchat DATE,\n" +
            "\tdepenseSup INTEGER NOT NULL DEFAULT 0,\n" +
            "\tsomDepensee INTEGER NOT NULL DEFAULT 0 " +
            ");",

            "CREATE TABLE IF NOT EXISTS InfoAppro(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\trefArticle TEXT,\n" +
            "\tidPanier INTEGER,\n" +
            "\tnbAchetes INTEGER NOT NULL,\n" +
            "\tprixGlobal INTEGER NOT NULL,\n" +
            "\t\n" +
            "\tFOREIGN KEY(refArticle) REFERENCES Article(ref),\n" +
            "\tFOREIGN KEY(idPanier) REFERENCES PanierAchats(id)\n" +
            ");",


            "CREATE TABLE IF NOT EXISTS Crediteur(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tsomDifference INTEGER NOT NULL,\n" +
            "\tnomCrediteur TEXT NOT NULL,\n" +
            "\tchbreCrediteur TEXT NOT NULL\n" +
            ");",

            "CREATE TABLE IF NOT EXISTS Debiteur(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tdetteDebiteur INTEGER NOT NULL,\n" +
            "\tnomDebiteur TEXT NOT NULL,\n" +
            "\tchbreDebiteur TEXT NOT NULL\n" +
            ");\n",

            "CREATE TABLE IF NOT EXISTS InfoEmprunt(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\trefArticle TEXT,\n" +
            "\tidDebiteur INTEGER,\n" +
            "\tqtiteEmprunte INTEGER NOT NULL,\n" +
            "\tprixUnitaireArticle INTEGER NOT NULL,\n" +
            "\tdateEmprunt DATE NOT NULL,\n" +
            "\tsommeRemboursee INTEGER NOT NULL DEFAULT 0, " +
            "\tsommeDue INTEGER NOT NULL, " +
            "\tFOREIGN KEY(refArticle) REFERENCES Article(ref),\n" +
            "\tFOREIGN KEY(idDebiteur) REFERENCES Debiteur(id)\n" +
            ");",

            "CREATE TABLE IF NOT EXISTS Journal(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tdateJournal DATE,\n" +
            "\ttotalPertes INTEGER DEFAULT 0," +
            "\ttotalVentes INTEGER DEFAULT 0" +
            ");",

            "CREATE TABLE IF NOT EXISTS FicheVenteArticle(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tdateJournal DATE,\n" +
            "\trefArticle TEXT,\n" +
            "\tqtiteVendus INTEGER NOT NULL,\n" +
            "\tqtitePerdus INTEGER DEFAULT 0,\n" +
            "\tprixUnitaire INTEGER NOT NULL,\n" +
            "\tmontantPertes INTEGER DEFAULT 0,\n" +
            "\tmontantVentes INTEGER DEFAULT 0,\n" +
            "\tFOREIGN KEY(dateJournal) REFERENCES Journal(dateJournal),\n" +
            "\tFOREIGN KEY(refArticle) REFERENCES Article(ref)\n" +
            ");",

            "CREATE TABLE IF NOT EXISTS FicheVenteCombo(\n" +
            "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\tdateJournal DATE,\n" +
            "\trefCombo TEXT,\n" +
            "\tqtiteVendus INTEGER NOT NULL,\n" +
            "\tqtitePerdus INTEGER DEFAULT 0,\n" +
            "\tprixUnitaire INTEGER NOT NULL,\n" +
            "\tmontantPertes INTEGER DEFAULT 0,\n" +
            "\tmontantVentes INTEGER DEFAULT 0,\n" +
            "\tFOREIGN KEY(dateJournal) REFERENCES Journal(dateJournal),\n" +
            "\tFOREIGN KEY(refCombo) REFERENCES Combo(ref)\n" +
            ");"
    };


    /* format de date AAAA-MM-JJ */

    /**
     * Gestionnaire de la Base de données
     * @param context le contexte (activité)
     */
    public MyDBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            for (String req: CREATE_REQ) {
                System.out.println("************ \n"+ req + "\n*******************");
                db.execSQL(req);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        for (String tb: TABLES) {
            removeTable(tb);
        }
        onCreate(db);
    }

    /**
     * Exécuter une requête SQL sans retour (INSERT, UPDATE, etc)
     * @param req la requête
     */
    public boolean execSQLNoReturn(String req){
        SQLiteDatabase db =  this.getWritableDatabase();
        try {
            db.execSQL(req);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * lancer une requête 'SELECT' avec des paramètres.<br>
     * les valeurs des arguments seront notés dans la requête par un '?'
     * @param sqlReq la requête
     * @param arguments les arguments
     * @return un curseur
     */
    public Cursor execSQLReturn(String sqlReq, String[] arguments){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(sqlReq, arguments);
    }

    /**
     * ajouter un enregistrement dans une Table
     * @param tableName nom de la table
     * @param tableValues tableau associatif représentant les attributs ainsi que leurs valeurs
     */
    public boolean insertData(String tableName, Map<String, String> tableValues){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> e: tableValues.entrySet()){
            values.put(e.getKey(), e.getValue());
        }

        long result = db.insert(tableName, null, values);

        return result != -1;
    }

    /**
     * Charger toutes les données d'une table
     * @param tableName nom de la table
     * @param order de récupération des éléments
     * @return la liste des éléments
     */
    public Cursor getAllData(String tableName, String order){
        String req = " SELECT * FROM " + tableName ;

        if(!(order == null)){
            req += " ORDER BY " + order;
        }

        return this.execSQLReturn(req, null);
    }

    /**
     * mettre à jour un enregistrement d'une table
     * @param pKName nom de l'attribut correspondant à la clé primaire
     * @param oldPKValue ancienne valeur de la clé primaire
     * @param tableName nom de la table
     * @param tableValues tableau associatif représentant l'ensemble des attributs ainsi que leurs valeurs (par rapport à l'objet)
     */
    public boolean updateData(String tableName, String pKName, String oldPKValue, Map<String, String> tableValues){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> e: tableValues.entrySet()){
            values.put(e.getKey(), e.getValue());
        }

        try {
            db.update(tableName, values, pKName + " = ? ", new String[]{oldPKValue});
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Retirer un élément d'une table
     * @param pKName nom de la clé primaire de la table
     * @param tableName la table
     * @param pKValue  valeur de la clé primaire de la table
     */
    public boolean removeData(String pKName, Integer pKValue, String tableName){
        return  this.execSQLNoReturn("DELETE FROM "
                    + tableName + " WHERE "
                    + pKName + " = " + pKValue.toString());
    }

    /**
     * Retirer un élément d'une table
     * @param pKName nom de la clé primaire de la table
     * @param tableName la table
     * @param pKValue  valeur de la clé primaire de la table
     */
    public boolean removeData(String pKName, String pKValue, String tableName){
        return this.execSQLNoReturn("DELETE FROM "
                    + tableName + " WHERE "
                    + pKName + " = " + pKValue);
    }

    /**
     * Retirer une table de la BDD
     * @param tbName nom de la table
     */
    public void removeTable(String tbName){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + tbName);
    }

}

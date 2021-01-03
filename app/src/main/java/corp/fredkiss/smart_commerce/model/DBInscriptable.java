package corp.fredkiss.smart_commerce.model;

import java.util.Map;

/**
 * Interface décrivant le comportement des Classes-Tables
 */
public interface DBInscriptable {
    /**
     * Récupérer tous les attributs d'un enregistrement de la Classe-table
     * @return attributs de la table
     */
    Map<String, String> getAllData();

    /**
     * Récupérer le nom de l'identifiant de la Classe-Table
     * @return le nom de son identifiant
     */
    String getIdName();
}

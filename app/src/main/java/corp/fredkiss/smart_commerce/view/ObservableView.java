package corp.fredkiss.smart_commerce.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface pour implémenter le design pattern 'observer'
 */
public interface ObservableView {

    List<ObserverView> observersList = new ArrayList<>();

    /**
     * Récupérer la liste des observateurs (Observers)
     * @return liste des observers
     */
    List<ObserverView> getObserverList();
}

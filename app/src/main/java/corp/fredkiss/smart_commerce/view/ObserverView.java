package corp.fredkiss.smart_commerce.view;

public interface ObserverView {
    /**
     * Update the observer
     * @param arg the string key that describes the operation which will be made
     * @param obj an optional argument needed by the operation which will be made
     * @throws Exception if arg is null
     */
    void querry(String arg, Object obj) throws Exception;
    /**
     * Get a state of the observable
     * @param querry a key that represents the state we need
     * @return the state querried
     */
    Object getObserverState(String querry);
}

package corp.fredkiss.smart_commerce.controller;

public class ElementNotExistException extends Exception {
    public ElementNotExistException(String message) {
        super(message);
    }
}

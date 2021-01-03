package corp.fredkiss.smart_commerce.model;

public class NotEnoughStockException extends Exception {
    public NotEnoughStockException(String s) {
        super(s);
    }
}

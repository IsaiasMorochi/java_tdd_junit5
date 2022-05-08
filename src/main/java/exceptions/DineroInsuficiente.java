package exceptions;

public class DineroInsuficiente extends RuntimeException {
    public DineroInsuficiente(String message) {
        super(message);
    }
}

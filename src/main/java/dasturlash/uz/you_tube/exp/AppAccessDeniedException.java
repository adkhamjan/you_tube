package dasturlash.uz.you_tube.exp;

public class AppAccessDeniedException extends RuntimeException {
    public AppAccessDeniedException(String message) {
        super(message);
    }
}
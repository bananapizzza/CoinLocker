package CoinLocker;

public class InvalidPasswordLimitReachedException extends Exception {
    public InvalidPasswordLimitReachedException(String errorMessage) {
        super(errorMessage);
    }
}

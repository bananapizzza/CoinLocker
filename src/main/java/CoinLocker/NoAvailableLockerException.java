package CoinLocker;

public class NoAvailableLockerException extends Exception {
    public NoAvailableLockerException(String errorMessage){
        super(errorMessage);
    }
}

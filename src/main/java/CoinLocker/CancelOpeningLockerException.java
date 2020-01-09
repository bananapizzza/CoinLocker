package CoinLocker;

public class CancelOpeningLockerException extends Exception {
    public CancelOpeningLockerException(String errorMessage){
        super(errorMessage);
    }
}

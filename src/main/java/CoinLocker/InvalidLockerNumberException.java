package CoinLocker;

public class InvalidLockerNumberException extends Exception {
    public InvalidLockerNumberException(String errorMessage){
        super(errorMessage);
    }
}

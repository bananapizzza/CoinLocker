package CoinLocker;

public class InvalidLockerSizeException extends Exception {
    public InvalidLockerSizeException(String errorMessage){
        super(errorMessage);
    }
}

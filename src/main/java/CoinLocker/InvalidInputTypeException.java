package CoinLocker;

public class InvalidInputTypeException extends Exception {
    public InvalidInputTypeException(String errorMessage){
        super(errorMessage);
    }
}

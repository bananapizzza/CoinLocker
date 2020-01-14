package CoinLocker;

public interface GetUserInput {
    String getString();
    int getInt() throws InvalidInputTypeException;
    void cleanResources();
}

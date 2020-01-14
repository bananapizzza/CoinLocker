package CoinLocker;

/**
 * This interface is used to get user's input.
 */
public interface GetUserInput {
    /**
     * @return  Return the entered string from user
     */
    String getString();

    /**
     * @return  Return the entered integer from user
     * @throws InvalidInputTypeException    If the input is not integer, it throws the exception
     */
    int getInt() throws InvalidInputTypeException;

    /**
     * Clean the resources used for getting user's input
     */
    void cleanResources();
}

package CoinLocker;

import java.util.Scanner;

public class GetUserInputWithScanner implements GetUserInput {
    Scanner scanner = new Scanner(System.in);

    @Override
    public String getString() {
        String s = scanner.nextLine();
        return s;
    }

    @Override
    public int getInt() throws InvalidInputTypeException {
        int i;
        if(scanner.hasNextInt()){
            i = scanner.nextInt();
            scanner.nextLine();
            return i;
        } else {
            String err = scanner.nextLine();
            throw new InvalidInputTypeException("Invalid input type. Type 'int' was expected. But "+err+" was entered.");
        }
    }

    @Override
    public void cleanResources() {
        scanner.close();
    }
}

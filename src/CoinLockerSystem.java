import lombok.Getter;

import java.security.InvalidParameterException;
import java.util.*;

@Getter
public class CoinLockerSystem {
    private int count = 1;
    private List<Locker> smallLockers;
    private List<Locker> mediumLockers;
    private List<Locker> largeLockers;
    private Map<Integer, Locker> usingLockers;
    private Scanner scanner;

    CoinLockerSystem() {
        smallLockers = new LinkedList<>();
        mediumLockers = new LinkedList<>();
        largeLockers = new LinkedList<>();
        usingLockers = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    CoinLockerSystem(int numOfSmall, int numOfMedium, int numOfLarge) {
        this();
        addSmallLockers(numOfSmall);
        addMediumLockers(numOfMedium);
        addLargeLockers(numOfLarge);
    }

    public void startCoinLockerSystem() {
        int choice = 0;

        System.out.println("Welcome to our locker system.");
        while (choice != 3) {
            printMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    rentLocker();
                    break;
                case 2:
                    openLocker();
                    break;
                case 3:
                    break;
            }
        }
        System.out.println("Thank you for using our locker system.");
    }

    private void printMenu() {
        System.out.println("Choose number you want.");
        System.out.println("1. Rent Locker");
        System.out.println("2. Open Locker");
        System.out.println("3. Exit");
    }

    private void rentLocker() {
        LockerSize size = getLockerSize();
        Locker locker = rentLocker(size);
        if (locker == null) {
            System.out.println("No locker is available.");
            return;
        }
        System.out.println("Your locker number is " + locker.getId());
        System.out.println("Price is " + locker.getPrice());
        getMoney(locker.getPrice());
        savePassword(locker);
        System.out.println("Locker number " + locker.getId() + " is opened!");
    }

    private LockerSize getLockerSize() {
        System.out.println("What size do you want?");
        printSize();
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                return LockerSize.SMALL;
            case 2:
                return LockerSize.MEDIUM;
            case 3:
                return LockerSize.LARGE;
        }
        return null;
    }

    private void printSize() {
        System.out.println("1. Small");
        System.out.println("2. Medium");
        System.out.println("3. Large");
    }

    Locker rentLocker(LockerSize size) {
        Locker locker = getAvailableLocker(size);
        return locker;
    }

    private Locker getAvailableLocker(LockerSize size) {
        Locker locker = null;
        switch (size) {
            case SMALL:
                if (smallLockers.size() > 0) {
                    locker = smallLockers.get(0);
                    smallLockers.remove(locker);
                    break;
                }
            case MEDIUM:
                if (mediumLockers.size() > 0) {
                    locker = mediumLockers.get(0);
                    mediumLockers.remove(locker);
                    break;
                }
            case LARGE:
                if (largeLockers.size() > 0) {
                    locker = largeLockers.get(0);
                    largeLockers.remove(locker);
                    break;
                }
        }
        if (locker != null) {
            usingLockers.put(locker.getId(), locker);
        }
        return locker;
    }

    private void getMoney(int price) {
        double paid = 0;
        do {
            System.out.println("Insert " + (price - paid) + " dollars.");
            paid += scanner.nextDouble();
            scanner.nextLine();
        } while (paid < price);
        if (paid > price) {
            System.out.println("Here is your change " + (paid - price) + " dollars");
        }
    }

    private void savePassword(Locker locker) {
        System.out.println("Enter a password for your locker");
        String password = scanner.nextLine();
        locker.setPassword(password);
    }

    private void openLocker() {
        Locker locker = null;
        System.out.println("Enter your locker number.");
        int lockerNum = scanner.nextInt();
        scanner.nextLine();
        try {
            locker = openLocker(lockerNum);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid locker number.");
            System.out.println("1. Enter the number again");
            System.out.println("2. Cancel");
            int choice = scanner.nextInt();
            if (choice == 1) {
                openLocker();
            } else {
                return;
            }
        }
        try {
            checkPassword(locker);
        } catch (InvalidParameterException e) {
            System.out.println("You entered wrong password 3 times. Please contact to the office.");
            return;
        }
        System.out.println("Locker number " + lockerNum + " is opened!");
        returnLocker(locker);
    }

    Locker openLocker(int lockerNum) {
        if (!usingLockers.containsKey(lockerNum)) {
            throw new IllegalArgumentException(
                    String.format("Given locker number [%d] doesn't exist.", lockerNum));
        }
        return usingLockers.get(lockerNum);
    }

    private void checkPassword(Locker locker) {
        System.out.println("Enter the password for this locker");
        int count = 0;
        while (count < 3) {
            String password = scanner.nextLine();
            if (password.equals(locker.getPassword())) {
                return;
            }
            System.out.println("Password is not matched. Try again.");
            count++;
        }
        if (count == 3) {
            throw new InvalidParameterException("Entered password is not matched over 3 times.");
        }
    }

    private void returnLocker(Locker locker) {
        usingLockers.remove(locker.getId());
        String lockerType = locker.getClass().getName();
        if (lockerType.equals("SmallLocker")) {
            smallLockers.add(locker);
        } else if (lockerType.equals("MediumLocker")) {
            mediumLockers.add(locker);
        } else {
            largeLockers.add(locker);
        }
    }


    public void addSmallLockers(int num) {
        for (int i = 0; i < num; i++) {
            smallLockers.add(new SmallLocker(count++));
        }
    }

    public void addMediumLockers(int num) {
        for (int i = 0; i < num; i++) {
            mediumLockers.add(new MediumLocker(count++));
        }
    }

    public void addLargeLockers(int num) {
        for (int i = 0; i < num; i++) {
            largeLockers.add(new LargeLocker(count++));
        }
    }
}

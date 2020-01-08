import lombok.Getter;

import java.util.*;

@Getter
public class CoinLockerSystem {
    private int count = 1;
    private List<Locker> smallLockers;
    private List<Locker> mediumLockers;
    private List<Locker> largeLockers;
    private Map<Integer, Locker> usingLockers;

    CoinLockerSystem() {
        smallLockers = new LinkedList<>();
        mediumLockers = new LinkedList<>();
        largeLockers = new LinkedList<>();
        usingLockers = new HashMap<>();
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
        Scanner scanner = new Scanner(System.in);
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
    }

    private void getMoney(int price) {
        double paid = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Insert " + (price - paid) + " dollars.");
            paid += scanner.nextDouble();
        } while (paid < price);
        if (paid > price) {
            System.out.println("Here is your change " + (paid - price) + " dollars");
        }
    }

    private LockerSize getLockerSize() {
        System.out.println("What size do you want?");
        printSize();
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
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


    private static void openLocker() {

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

    public Locker rentLocker(LockerSize size) {
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
                }
                break;
            case MEDIUM:
                if (mediumLockers.size() > 0) {
                    locker = mediumLockers.get(0);
                    mediumLockers.remove(locker);
                }
                break;
            case LARGE:
                if (largeLockers.size() > 0) {
                    locker = largeLockers.get(0);
                    largeLockers.remove(locker);
                }
                break;
        }
        if(locker != null){
            usingLockers.put(locker.getId(), locker);
        }
        return locker;
    }
}

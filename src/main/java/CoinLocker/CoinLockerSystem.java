package CoinLocker;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoinLockerSystem {

    public final int INVALID_PW_LIMIT = 3;
    public final int SMALL_LOCKER = 1;
    public final int MEDIUM_LOCKER = 2;
    public final int LARGE_LOCKER = 3;
    public final int CANCEL = 4;
    int nextId = 1;

    @NonNull
    GetUserInput getUserInput;
    int numberOfSmallLockers;
    int numberOfMediumLockers;
    int numberOfLargeLockers;


    final List<Locker> smallLockers = new ArrayList<>();
    final List<Locker> mediumLockers = new ArrayList<>();
    final List<Locker> largeLockers = new ArrayList<>();
    final Map<Integer, Locker> lockersInUse = new HashMap<>();

    @Builder(toBuilder = true)
    private CoinLockerSystem(GetUserInput getUserInput,
                             int numberOfSmallLockers,
                             int numberOfMediumLockers,
                             int numberOfLargeLockers) {
        this.getUserInput = getUserInput;
        this.numberOfSmallLockers = numberOfSmallLockers;
        this.numberOfMediumLockers = numberOfMediumLockers;
        this.numberOfLargeLockers = numberOfLargeLockers;

        addSmallLockers(numberOfSmallLockers);
        addMediumLockers(numberOfMediumLockers);
        addLargeLockers(numberOfLargeLockers);
    }

    public void startCoinLockerSystem() {
        int choice = 0;

        System.out.println("Welcome to our locker system.");
        while (choice != 3) {
            printMenu();
            try {
                choice = getUserInput.getInt();
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
            } catch (InvalidInputTypeException e) {
                System.out.println("Invalid input was entered. Please try again.");
            }
        }
        getUserInput.cleanResources();
        System.out.println("Thank you for using our locker system.");
    }

    private void printMenu() {
        System.out.println("Choose number you want.");
        System.out.println("1. Rent Locker");
        System.out.println("2. Open Locker");
        System.out.println("3. Exit");
    }

    Locker rentLocker() {
        Locker locker = null;
        try {
            LockerSize size = getLockerSize();
            locker = getAvailableLocker(size);
            System.out.println("Your locker number is " + locker.getId());
            System.out.println("Price is " + locker.getPrice());
            getMoney(locker.getPrice());
            savePassword(locker);
            System.out.println("Locker number " + locker.getId() + " is opened!");
            moveLockerToInUseList(locker);
        } catch (CancelRentingLockerException e) {
            System.out.println("Cancel renting a locker");
        } catch (NoAvailableLockerException e) {
            System.out.println("No locker is available.");
        } catch (InvalidLockerSizeException e) {
            System.out.println("Invalid locker size parameter passed.");
        }
        return locker;
    }


    LockerSize getLockerSize() throws CancelRentingLockerException {
        System.out.println("What size do you want?");
        printSize();
        int input = 0;
        try {
            input = getUserInput.getInt();
            switch (input) {
                case 1:
                    return LockerSize.SMALL;
                case 2:
                    return LockerSize.MEDIUM;
                case 3:
                    return LockerSize.LARGE;
                case 4:
                    throw new CancelRentingLockerException("User canceled to select locker size");
                default:
                    System.out.println("Wrong size. Select again.");
                    return getLockerSize();
            }
        } catch (InvalidInputTypeException e) {
            System.out.println("Invalid input was entered. Please try again.");
            return getLockerSize();
        }
    }

    private void printSize() {
        System.out.println(SMALL_LOCKER + ". Small");
        System.out.println(MEDIUM_LOCKER + ". Medium");
        System.out.println(LARGE_LOCKER + ". Large");
        System.out.println(CANCEL + ". Cancel");
    }

    Locker getAvailableLocker(LockerSize size) throws InvalidLockerSizeException, NoAvailableLockerException {
        Locker locker = null;
        switch (size) {
            case SMALL:
                if (smallLockers.size() > 0) {
                    locker = smallLockers.get(0);
                } else {
                    return getAvailableLocker(LockerSize.MEDIUM);
                }
                break;
            case MEDIUM:
                if (mediumLockers.size() > 0) {
                    locker = mediumLockers.get(0);
                } else {
                    return getAvailableLocker(LockerSize.LARGE);
                }
                break;
            case LARGE:
                if (largeLockers.size() > 0) {
                    locker = largeLockers.get(0);
                }
                break;
            default:
                throw new InvalidLockerSizeException("Invalid locker size parameter" + size + " has passed.");
        }
        if (locker == null) {
            throw new NoAvailableLockerException("No empty lockers.");
        }
        return locker;
    }

    void getMoney(int price) throws CancelRentingLockerException {
        double paid = 0;
        do {
            System.out.println("Insert " + (price - paid) + " dollars or enter # to cancel.");
            String choice = getUserInput.getString();
            switch (choice) {
                case "#":
                    giveChange(0, paid);
                    throw new CancelRentingLockerException("User canceled to insert money");
                default:
                    paid += Double.valueOf(choice);
            }
        } while (paid < price);
        giveChange(price, paid);
    }

    void giveChange(int price, double paid) {
        if (paid > price) {
            System.out.println("Here is your change " + (paid - price) + " dollars.");
        }
    }

    void savePassword(Locker locker) {
        System.out.println("Enter a 4-digit password for your locker");
        String password = getUserInput.getString();
        if (isValidPassword(password)) {
            locker.setPassword(password);
        } else {
            System.out.println("Invalid password. Please try again.");
            savePassword(locker);
        }
    }

    boolean isValidPassword(String password) {
        String regex = "\\d{4}";
        return password.matches(regex);
    }

    void moveLockerToInUseList(Locker locker) {
        lockersInUse.put(locker.getId(), locker);
        if (locker instanceof SmallLocker) {
            smallLockers.remove(locker);
        } else if (locker instanceof MediumLocker) {
            mediumLockers.remove(locker);
        } else if (locker instanceof LargeLocker) {
            largeLockers.remove(locker);
        }
    }

    void openLocker() {
        Locker locker = null;
        try {
            int lockerNum = getLockerNumber();
            locker = getInUseLocker(lockerNum);
            checkPassword(locker);
            System.out.println("Locker number " + lockerNum + " is opened!");
            returnLocker(locker);
        } catch (CancelOpeningLockerException e) {
            System.out.println("Cancel opening a locker.");
        } catch (InvalidPasswordLimitReachedException e) {
            System.out.println("You entered wrong password " + INVALID_PW_LIMIT + " times. Please contact to the office.");
        }
    }

    int getLockerNumber() throws CancelOpeningLockerException {
        System.out.println("Enter your locker number or enter # to cancel.");
        String choice = getUserInput.getString();
        int lockerNum;
        switch (choice) {
            case "#":
                throw new CancelOpeningLockerException("User canceled to open a locker");
            default:
                lockerNum = Integer.valueOf(choice);
        }
        if (!isValidLockerNumber(lockerNum)) {
            System.out.println("Invalid locker number.");
            return getLockerNumber();
        }
        return lockerNum;
    }

    boolean isValidLockerNumber(int lockerNum) {
        if (lockersInUse.containsKey(lockerNum)) {
            return true;
        }
        return false;
    }

    Locker getInUseLocker(int lockerNum) {
        return lockersInUse.get(lockerNum);
    }

    void checkPassword(Locker locker) throws InvalidPasswordLimitReachedException {
        System.out.println("Enter the password for this locker");
        int count = 0;
        String lockerPassword = locker.getPassword();
        while (count < INVALID_PW_LIMIT) {
            String password = getUserInput.getString();
            if (password.equals(lockerPassword)) {
                return;
            }
            System.out.println("Password is not matched. Try again.");
            count++;
        }
        throw new InvalidPasswordLimitReachedException("Entered password is not matched over " + INVALID_PW_LIMIT + " times.");
    }

    void returnLocker(Locker locker) {
        lockersInUse.remove(locker.getId());
        if (locker instanceof SmallLocker) {
            smallLockers.add(locker);
        } else if (locker instanceof MediumLocker) {
            mediumLockers.add(locker);
        } else if (locker instanceof LargeLocker) {
            largeLockers.add(locker);
        }
    }


    public void addSmallLockers(int num) {
        for (int i = 0; i < num; i++) {
            smallLockers.add(new CoinLocker.SmallLocker(nextId++));
        }
    }

    public void addMediumLockers(int num) {
        for (int i = 0; i < num; i++) {
            mediumLockers.add(new CoinLocker.MediumLocker(nextId++));
        }
    }

    public void addLargeLockers(int num) {
        for (int i = 0; i < num; i++) {
            largeLockers.add(new CoinLocker.LargeLocker(nextId++));
        }
    }
}

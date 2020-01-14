package CoinLocker;

public class Main {
    public static void main(String[] args) {
        CoinLocker.CoinLockerSystem coinLockerSystem = CoinLocker.CoinLockerSystem.builder()
                .numberOfSmallLockers(3)
                .numberOfMediumLockers(3)
                .numberOfLargeLockers(3)
                .getUserInput(new GetUserInputWithScanner())
                .build();
        coinLockerSystem.startCoinLockerSystem();
    }
}

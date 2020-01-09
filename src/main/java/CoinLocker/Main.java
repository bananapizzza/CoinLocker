package CoinLocker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CoinLocker.CoinLockerSystem coinLockerSystem = CoinLocker.CoinLockerSystem.builder()
                .numberOfSmallLockers(3)
                .numberOfMediumLockers(3)
                .numberOfLargeLockers(3)
                .scanner(new Scanner(System.in))
                .build();
        coinLockerSystem.startCoinLockerSystem();
    }
}

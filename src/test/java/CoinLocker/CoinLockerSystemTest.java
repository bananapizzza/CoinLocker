package CoinLocker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class CoinLockerSystemTest {

    CoinLockerSystem coinLockerSystem;

    @BeforeEach
    void initialize() {
        coinLockerSystem = CoinLockerSystem.builder()
                .numberOfSmallLockers(1)
                .numberOfMediumLockers(1)
                .numberOfLargeLockers(1)
                .scanner(new Scanner(System.in))
                .build();
    }

    @Test
    void test_addLockers() {
        coinLockerSystem.addSmallLockers(1);
        coinLockerSystem.addMediumLockers(2);
        coinLockerSystem.addLargeLockers(3);

        Assertions.assertEquals(2, coinLockerSystem.getSmallLockers().size());
        Assertions.assertEquals(3, coinLockerSystem.getMediumLockers().size());
        Assertions.assertEquals(4, coinLockerSystem.getLargeLockers().size());
    }

    @Test
    void test_rentLocker() {
        try {
            Locker locker = coinLockerSystem.getAvailableLocker(LockerSize.SMALL);
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.SmallLocker", locker.getClass().getName());

            locker = coinLockerSystem.getAvailableLocker(LockerSize.SMALL);
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.MediumLocker", locker.getClass().getName());

            locker = coinLockerSystem.getAvailableLocker(LockerSize.SMALL);
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.LargeLocker", locker.getClass().getName());

            Assertions.assertThrows(NoAvailableLockerException.class, () -> coinLockerSystem.getAvailableLocker(LockerSize.SMALL));
        } catch (NoAvailableLockerException e) {
            e.printStackTrace();
        } catch (InvalidLockerSizeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_getMoney() {
        int desiredPrice = 3;
        try {
            coinLockerSystem.getMoney(3);
        } catch (CancelRentingLockerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_isValidPassword() {
        String test = "2028";
        Assertions.assertTrue(coinLockerSystem.isValidPassword(test));
        test = "39d";
        Assertions.assertFalse(coinLockerSystem.isValidPassword(test));
        test = "29349";
        Assertions.assertFalse(coinLockerSystem.isValidPassword(test));
    }

//    @Test
//    void test_openLocker() {
//        try {
//            Locker locker = coinLockerSystem.rentLocker(LockerSize.SMALL);
//
//        } catch (NoAvailableLockerException e) {
//            e.printStackTrace();
//        }
//    }
}
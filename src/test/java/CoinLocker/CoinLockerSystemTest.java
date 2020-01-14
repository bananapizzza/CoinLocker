package CoinLocker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CoinLockerSystemTest {

    @Mock
    GetUserInput getUserInputForTest;

    CoinLockerSystem coinLockerSystem;

    @BeforeEach
    void initialize() {
        coinLockerSystem = CoinLockerSystem.builder()
                .numberOfSmallLockers(1)
                .numberOfMediumLockers(1)
                .numberOfLargeLockers(1)
                .getUserInput(getUserInputForTest)
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
    void test_getAvailableLocker() {
        try {
            Locker locker = coinLockerSystem.getAvailableLocker(LockerSize.SMALL);
            Assertions.assertNotNull(locker);
            Assertions.assertEquals("CoinLocker.SmallLocker", locker.getClass().getName());

            locker = coinLockerSystem.getAvailableLocker(LockerSize.MEDIUM);
            Assertions.assertNotNull(locker);
            Assertions.assertEquals("CoinLocker.MediumLocker", locker.getClass().getName());

            locker = coinLockerSystem.getAvailableLocker(LockerSize.LARGE);
            Assertions.assertNotNull(locker);
            Assertions.assertEquals("CoinLocker.LargeLocker", locker.getClass().getName());
        } catch (InvalidLockerSizeException e) {
            e.printStackTrace();
        } catch (NoAvailableLockerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_getLockerSize() {
        try {
            doReturn(coinLockerSystem.SMALL_LOCKER).when(getUserInputForTest).getInt();
            Assertions.assertEquals(LockerSize.SMALL, coinLockerSystem.getLockerSize());

            doReturn(coinLockerSystem.MEDIUM_LOCKER).when(getUserInputForTest).getInt();
            Assertions.assertEquals(LockerSize.MEDIUM, coinLockerSystem.getLockerSize());

            doReturn(coinLockerSystem.LARGE_LOCKER).when(getUserInputForTest).getInt();
            Assertions.assertEquals(LockerSize.LARGE, coinLockerSystem.getLockerSize());
        } catch (InvalidInputTypeException e) {
            e.printStackTrace();
        } catch (CancelRentingLockerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_getMoney() {
        try {
            int desiredPrice = 3;
            doReturn("5").when(getUserInputForTest).getString();
            coinLockerSystem.getMoney(desiredPrice);

            doReturn("#").when(getUserInputForTest).getString();
            Assertions.assertThrows(CancelRentingLockerException.class, () -> coinLockerSystem.getMoney(desiredPrice));
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

    @Test
    void test_savePassword() {
        Locker locker = new SmallLocker(100);
        doReturn("1234").when(getUserInputForTest).getString();

        coinLockerSystem.savePassword(locker);
        Assertions.assertEquals("1234", locker.getPassword());
    }

    @Test
    void test_moveLockerToInUseList() {
        Locker locker = coinLockerSystem.getSmallLockers().get(0);
        coinLockerSystem.moveLockerToInUseList(locker);

        Assertions.assertEquals(coinLockerSystem.getLockersInUse().get(locker.getId()), locker);
        Assertions.assertEquals(0, coinLockerSystem.getSmallLockers().size());
    }

    @Test
    void test_rentLocker() {
        try {
            doReturn(coinLockerSystem.SMALL_LOCKER).when(getUserInputForTest).getInt();
            doReturn("5", "1234").when(getUserInputForTest).getString();
            Locker locker = coinLockerSystem.rentLocker();
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.SmallLocker", locker.getClass().getName());

            locker = coinLockerSystem.rentLocker();
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.MediumLocker", locker.getClass().getName());

            locker = coinLockerSystem.rentLocker();
            Assertions.assertNotEquals(null, locker);
            Assertions.assertEquals("CoinLocker.LargeLocker", locker.getClass().getName());

            Assertions.assertThrows(NoAvailableLockerException.class, () -> coinLockerSystem.getAvailableLocker(LockerSize.SMALL));
        } catch (InvalidInputTypeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_getLockerNumber() {
        try {
            Locker locker = new SmallLocker(100);
            coinLockerSystem.getLockersInUse().put(locker.getId(), locker);

            doReturn(String.valueOf(locker.getId())).when(getUserInputForTest).getString();
            Assertions.assertEquals(locker.getId(), coinLockerSystem.getLockerNumber());

            doReturn("#").when(getUserInputForTest).getString();
            Assertions.assertThrows(CancelOpeningLockerException.class, () -> coinLockerSystem.getLockerNumber());
        } catch (CancelOpeningLockerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_isValidLockerNumber() {
        Locker locker = new SmallLocker(100);
        coinLockerSystem.getLockersInUse().put(locker.getId(), locker);

        Assertions.assertTrue(coinLockerSystem.isValidLockerNumber(100));
        Assertions.assertFalse(coinLockerSystem.isValidLockerNumber(101));
    }

    @Test
    void test_checkPassword() {
        try {
            Locker locker = new SmallLocker(100);
            locker.setPassword("1234");

            doReturn("1234").when(getUserInputForTest).getString();
            coinLockerSystem.checkPassword(locker);

            doReturn("1111").when(getUserInputForTest).getString();
            Assertions.assertThrows(InvalidPasswordLimitReachedException.class, () -> coinLockerSystem.checkPassword(locker));
        } catch (InvalidPasswordLimitReachedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_returnLocker() {
        Locker locker = new SmallLocker(100);
        coinLockerSystem.getLockersInUse().put(locker.getId(), locker);

        coinLockerSystem.returnLocker(locker);
        Assertions.assertFalse(coinLockerSystem.getLockersInUse().containsKey(locker.getId()));
        Assertions.assertEquals(0, coinLockerSystem.getLockersInUse().size());
        Assertions.assertEquals(2, coinLockerSystem.getSmallLockers().size());
    }

    @Test
    void test_openLocker() {
        Locker locker = new SmallLocker(100);
        locker.setPassword("1234");
        coinLockerSystem.getLockersInUse().put(locker.getId(), locker);

        doReturn("100", "1234").when(getUserInputForTest).getString();

        coinLockerSystem.openLocker();
    }
}
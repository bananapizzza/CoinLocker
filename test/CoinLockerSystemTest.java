import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinLockerSystemTest {
    CoinLockerSystem cl;

    @BeforeEach
    public void initialize(){
        cl = new CoinLockerSystem(1, 1, 1);
    }

    @Test
    public void test_addLockers(){
        cl.addSmallLockers(1);
        cl.addMediumLockers(2);
        cl.addLargeLockers(3);

        assertEquals(2, cl.getSmallLockers().size());
        assertEquals(3, cl.getMediumLockers().size());
        assertEquals(4, cl.getLargeLockers().size());
    }

    @Test
    public void test_rentLocker(){
        Locker locker = cl.rentLocker(LockerSize.SMALL);
        assertNotEquals(null, locker);
        assertEquals("SmallLocker", locker.getClass().getName());

        locker = cl.rentLocker(LockerSize.SMALL);
        assertNotEquals(null, locker);
        assertEquals("MediumLocker", locker.getClass().getName());

        locker = cl.rentLocker(LockerSize.SMALL);
        assertNotEquals(null, locker);
        assertEquals("LargeLocker", locker.getClass().getName());

        locker = cl.rentLocker(LockerSize.SMALL);
        assertEquals(null, locker);
    }
}
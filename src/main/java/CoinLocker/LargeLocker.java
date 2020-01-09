package CoinLocker;

import lombok.Getter;

@Getter
public class LargeLocker extends Locker {
    LargeLocker(int id){
        super(id, 15, 3);
    }
}

package CoinLocker;

import lombok.Getter;

@Getter
public class SmallLocker extends Locker {
    SmallLocker(int id){
        super(id, 5, 1);
    }
}

package CoinLocker;

import lombok.Getter;

@Getter
public class MediumLocker extends Locker {
    MediumLocker(int id){
        super(id, 10, 2);
    }
}

package CoinLocker;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@Data
@RequiredArgsConstructor
public class Locker {
    @NonNull
    int id;
    @NonNull
    int size;
    @NonNull
    int price;
    @Nullable
    String password;
}

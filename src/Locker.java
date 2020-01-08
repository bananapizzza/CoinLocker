import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Locker {
    @NonNull
    private int id;
    @NonNull
    private int size;
    @NonNull
    private int price;
    private String password;
}

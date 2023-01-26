import java.util.Set;

public interface PreferencesService {
    Set<Preference> get(String userId);
}
enum Preference {
    FOOTBALL("Сыграть в футбол"),
    WALKING("Выйти на прогулку"),
    WATCHING_FILMS("Посмотреть кино дома"),
    READING("Почитать книгу");
    private final String value;
    Preference(String value) {
        this.value = value;
    }
}
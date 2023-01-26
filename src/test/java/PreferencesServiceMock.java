import java.util.Set;

public class PreferencesServiceMock implements PreferencesService{

    private Set<Preference> value;

    public PreferencesServiceMock() {//добавили конструктор по умолчанию возвращаем эти значения
        this.value = Set.of(Preference.READING, Preference.FOOTBALL);
    }

    public void setValue(Set<Preference> value) {
        this.value = value;
    }

    @Override
    public Set<Preference> get(String userId) {
        return value;
    }
}

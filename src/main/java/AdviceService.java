import java.util.Set;
import java.util.stream.Collectors;

public class AdviceService {
    private final PreferencesService preferencesService;
    private final WeatherService weatherService;

    public AdviceService(PreferencesService preferencesService, WeatherService weatherService) {
        this.preferencesService = preferencesService;
        this.weatherService = weatherService;
    }

    public Set<Preference> getAdvice(String userId) {//метод гетАдвайс работает: он получает погоду из погоды ниже)
        // и предпочтения еще ниже а дальше срабатывает фильтр(иф)
        Weather weather = weatherService.currentWeather();

        Set<Preference> preferences = preferencesService.get(userId);

        if (Weather.RAINY == weather || Weather.STORMY == weather) {//это фильт им проверяется если погода плохая либо дождливая либо ветренная
            return preferences.stream()
                    .filter(p -> p != Preference.FOOTBALL)//то фильтр удаляет футбол
                    .collect(Collectors.toSet());
        } else if (Weather.SUNNY == weather) {//а если погода солнечная то лучше не стоит читать книжки :)
            return preferences.stream()
                    .filter(p -> p != Preference.READING)
                    .collect(Collectors.toSet());
        }
        return preferences;
    }
}

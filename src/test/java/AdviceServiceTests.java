import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdviceServiceTests {

    @Test
    public  void test_get_advice_in_bad_weather(){
        //arrange
        WeatherServiceMock weatherService = new WeatherServiceMock();//implementation во первых создаем заглушки, во вторых конфигурируем их
        weatherService.setValue(Weather.STORMY);//если мы хотим протестировать адвайс сервис, мы должны посмотреть на его логику,
        // захоим в класс АдвайсСервис...пришли :) далее мы можем сконфигурировать ожидаемою погоду, и му уже знаем как работает фильтр,
        // в нашем случае будет фильтроваться футбол ибо не поиграеш в него в шторм

        PreferencesServiceMock preferencesService = new PreferencesServiceMock();//implementation
        preferencesService.setValue(Set.of(Preference.FOOTBALL, Preference.WATCHING_FILMS, Preference.READING));//указываем набор предпочтений, где у нас есть футбол

        AdviceService adviceService = new AdviceService(preferencesService, weatherService);

        Set<Preference> expected = Set.of(Preference.READING, Preference.WATCHING_FILMS);//указываем результат.
        // Указываем результат без футбола, тоесть у нас должен отработать фильтр

        //act
        Set<Preference> preferences = adviceService.getAdvice("user1");

        //assert
        Assertions.assertEquals(expected, preferences);

        //итак у нас нет реализации но мы уверенны что АдвайсСервис отработает на любой реализации, нам не важно где метод везеСервис будет брать данные, из интернета , нам не важно где преференс сервис будет доставать предпочтения пользователя например опять таки из базы данных
    }
}
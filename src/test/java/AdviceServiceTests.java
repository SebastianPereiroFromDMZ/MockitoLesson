import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdviceServiceTests {

    //wits our mocks (тест без мокито)
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

        //итак у нас нет реализации но мы уверенны что АдвайсСервис отработает на любой реализации, нам не важно где метод
        // везеСервис будет брать данные, из интернета , нам не важно где преференс сервис будет доставать предпочтения пользователя
        // например опять таки из базы данных, какой плюс у такого подхода? во первых нам не нужны никакие зависимости.
        // Никакой логико в заглушках нет это просто заглушка, в чем минус такого подхода, мы уже разбирались что тесты
        // должны быть устойчивы к рефрактенгу(переработке), если мы изменяем интерфейс одного из сервисов, нам придется
        // переписывать еще и заглушки. Каким образом мы можем этого избежать? Было бы здорово что бы у нас была библиотека,
        // которая умеет генерировать эти заглушки автоматически, тогда наши тесты будут устойчивы к рефракторингу, и заглушки тоже.
        // Так вот как раз этот функционал предоставляет библиотека мокито
    }

    //test with mockito
    @Test
    void test_get_advice_in_bad_weather_mockito(){
        //arrange

        Set<Preference> expected = Set.of(Preference.READING, Preference.WATCHING_FILMS);//ожидаемый результат

        WeatherService weatherService = Mockito.mock(WeatherService.class);//собственно создание заглушки производится методом Мокито.мок.
        // Мокито это импортируемый обьект из библиотеки мокито, мок это статический метод этого обьекта, и ме передаем в параметрах
        // тип обьекта того класса которого хотим заглушку
        Mockito.when(weatherService.currentWeather())//создали заглушку, однако сам мок не знает что возвращать и возвращает какието обьекты по умолчанию:
                // пустые обьекты, поэтому ему надо указать что возвращать, конфигурация заглушки производится с помощью методов when и thenReturn.
                // Соответсятвенно в when мы передаем метод, который мы хотим конфигурировать, а в thenReturn значение которое должно возвращатся.
                // Собственно вот эти три строчки кода заменяют 2 строчки кода из теста из предыдущего примера плюс
                // ПОЛНОСТЬЮ ИНТЕРФЕЙС который имплементируют те заглушки. И это еще не самый главный плюс,
                // главное теперь мы не зависим от изменений в этих интерфейсах)
                .thenReturn(Weather.STORMY);


        PreferencesService preferencesService = Mockito.mock(PreferencesService.class);
        Mockito.when(preferencesService.get("Петя"))
                .thenReturn(Set.of(Preference.FOOTBALL, Preference.READING, Preference.WATCHING_FILMS));


        AdviceService adviceService = new AdviceService(preferencesService, weatherService);

        //act
        Set<Preference> preferences = adviceService.getAdvice("Петя");

        //assert
        Assertions.assertEquals(expected, preferences);
    }

}
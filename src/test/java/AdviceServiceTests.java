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
        Mockito.when(preferencesService.get("Петя"))//Mockito.any() можно подставить метод для всех,
                // для какого то типа ну или как в нашем случае только для кого то
                .thenReturn(Set.of(Preference.FOOTBALL, Preference.READING, Preference.WATCHING_FILMS));


        AdviceService adviceService = new AdviceService(preferencesService, weatherService);

        //act
        Set<Preference> preferences = adviceService.getAdvice("Петя");

        //assert
        Assertions.assertEquals(expected, preferences);
    }


    //Тест и для моков и дли шпионов(СПАЙ)
    //Mockito verify
    @Test
    void test_get_advice_in_bad_weather_mockito_verify(){
        //здесь мы просто проверяем сколько раз и с какими параметрами вызвались методы в тестируюем классе АдвайсесСервис методы
        // currentWeather() и get(userId), это динственный способ протестировать метод который возвращает войд, другие вариаты мы уже не протестируем.
        // Мы можем посмотреть был ли вызов какойто зависимости или не был, если зависимость была под ИФом мы можем понятькакая ветка кода отработала

        //arrange
        WeatherService weatherService = Mockito.mock(WeatherService.class);//говорим вот у нас есть обьект
        Mockito.when(weatherService.currentWeather()).thenReturn(Weather.STORMY);//и он возвращает плохую погоду

        PreferencesService preferencesService = Mockito.mock(PreferencesService.class);//далее создаем второй обьект
        Mockito.when(preferencesService.get(Mockito.any())).thenReturn(Set.of(Preference.FOOTBALL));//конфигурируем что бы он возвращал футбол.
        // В принципе нам не важно что он возвращает потому что мы в этом тесте не будем проверять возвращаемые обьекты,
        // МЫ БУДЕМ ПРОВЕРЯТЬ СКОЛЬКО РАЗ КАКОЙ МЕТОД ЗАГЛУШКИ ВЫЗВАЛСЯ

        AdviceService adviceService = new AdviceService(preferencesService, weatherService);

        //act
        adviceService.getAdvice("user1");
        adviceService.getAdvice("user1");

        //assert
        Mockito.verify(preferencesService, Mockito.times(2)).get("user1");
        Mockito.verify(preferencesService, Mockito.times(0)).get("user2");//у мокито есть метод верифай который как раз позволяет
        // проверить сколько раз вызывался тоот или иной метод заглушки, при этом точно так жекак и в случае конфигурации возвращаемых значаний так же
        // есть привязка к параметрам. Что бы проверить сколько раз вызывался метод гетПреференсСервиса, мы должны написать МокитоВерифайПреференсСервис
        // и указать переменную мокито таймс(ожидаемое количество раз), далее идет гет, но в этом случае гет это не получение значения,
        // а наоборот мы даем параметр к которому будет применен выше описанный метод.
        // Соответственно мы задаем в верифай для юзера1 что метод гетПреференсСервис вызывался 1 раз, а для юзера2 0 раз(так как мы вообще его не забили)
    }
}
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
//допустим у нас есть не очень красивая реализация, у нас есть реализация какого либо интерфейса,
// но сам интерфейс не выделен, в этом случае можно использовать Мокито СПАЙ. Если мокито мок мы используем для интерфейсов,
// то для таких ситуация используем МОКИТО СПАЙ, он добавляет обертку которая дает дополнительный функционал для заглушки,
// при этом шпион(спай) пробрасывает вызов к реальныи обьектам, тоесть мы получаем дополнительную функциональность мокито,
// но все равно вызовы у нас будут пробрасываться

public class WeatherServiceImpl implements WeatherService {


    @Override
    public Weather currentWeather() {//конфигурируем погоду
        return Weather.SUNNY;
    }

    @Test
    void testSpyWeatherService(){

        WeatherService weatherService = Mockito.spy(WeatherServiceImpl.class);

        Mockito.when(weatherService.currentWeather()).thenReturn(Weather.STORMY);//переопределяем погоду на шторм,
        // мы указали что при вызове этого метода теперь шторм. Собственно у нас получился прокси обьект,
        // он возвращает то что мы ему указали а не то что есть по факту(по факту у нас погода солнечная)

        Assertions.assertEquals(Weather.STORMY, weatherService.currentWeather());//и теперь дальше мы сравниваем со штормом

        //Mockito.verify(weatherService, Mockito.only()).currentWeather();
    }
}

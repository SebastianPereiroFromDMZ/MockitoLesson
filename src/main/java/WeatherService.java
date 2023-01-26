public interface WeatherService {//интерфейс
    Weather currentWeather();
}

enum Weather {//перечисление
    RAINY("Дождливо"),
    STORMY("Сильный ветер"),
    SUNNY("Солнечно"),
    CLOUDY("Облачно");
    private String weather;
    Weather(String weather) {
        this.weather = weather;
    }
}
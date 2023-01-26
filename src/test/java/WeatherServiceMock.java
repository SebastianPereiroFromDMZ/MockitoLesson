public class WeatherServiceMock implements WeatherService{//собственно вот и наш класс заглушка, мокито это создание
    // так называемых заглушек (моков) с помощью которых мы можем писать модульные тесты ,
    // и менно например у нас тест привязан к какооту удаленному доступу, а у нас допустим нет интернета,
    // соответсявенно в тесте будет ошибка, поэтому для нашего облегчения есть библиотека Мокито, на ее основе создаем классы пустышки

    private Weather value;//добовляем поле в котором храниться текущее состояние нашей погоды, это нужно для того чтобы писать несколько тестов

    @Override
    public Weather currentWeather() {//ну а этот метод просто возвращает нам текущую погоду
        return value;
    }

    public void setValue(Weather value) {
        this.value = value;//здесь мы будем задавать нашу погоду
    }
}

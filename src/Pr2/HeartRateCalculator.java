public class HeartRateCalculator {
    private static HeartRateCalculator instance;

    // Приватний конструктор для запобігання створенню екземплярів ззовні
    private HeartRateCalculator() {
    }

    // Метод Singleton getInstance()
    public static HeartRateCalculator getInstance() {
        if (instance == null) {
            instance = new HeartRateCalculator();
        }
        return instance;
    }

    // Метод для розрахунку частоти серцевих скорочень на основі температури тіла і фізіологічної норми температури
    public double calculateHeartRate(double bodyTemperature, double physiologicalNormTemperature) {
        return (bodyTemperature - physiologicalNormTemperature) * 10;
    }
}

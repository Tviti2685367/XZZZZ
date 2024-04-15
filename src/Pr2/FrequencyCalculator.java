public class FrequencyCalculator {
    private static final FrequencyCalculator instance = new FrequencyCalculator();

    private FrequencyCalculator() {
        // Приватний конструктор для запобігання створенню екземплярів
    }

    public static FrequencyCalculator getInstance() {
        return instance;
    }

    public double calculateHeartRate(double bodyTemperature, double physiologicalNormTemperature) {
        return (bodyTemperature - physiologicalNormTemperature) * 10;
    }
}
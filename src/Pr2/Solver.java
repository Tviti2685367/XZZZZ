
import java.util.Scanner;

public class Solver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть температуру тіла:");
        double bodyTemperature = scanner.nextDouble();

        System.out.println("Введіть фізіологічну норму температури:");
        double physiologicalNormTemperature = scanner.nextDouble();

        HeartRateCalculator calculator = HeartRateCalculator.getInstance();
        double heartRate = calculator.calculateHeartRate(bodyTemperature, physiologicalNormTemperature);
        System.out.println("Пульс: " + heartRate + " ударів на хвилину");

        System.out.println("Введіть параметр відображення (1 - Текстова таблиця, 2 - HTML Таблиця):");
        int displayOption = scanner.nextInt();
        CalculationResultRenderer renderer;
        if (displayOption == 1) {
            renderer = new TextResultRenderer();
        } else {
            renderer = new HTMLTableResultRenderer();
        }

        CalculationResult result = new CalculationResult(bodyTemperature, physiologicalNormTemperature);
        String renderedResult = renderer.render(result);
        System.out.println(renderedResult);

        scanner.close();
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) {
        HeartRateCalculator heartRateCalculator = HeartRateCalculator.getInstance();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введіть температуру тіла:");
            double bodyTemperature = scanner.nextDouble();

            System.out.println("Введіть фізіологічну норму температури:");
            double physiologicalNormTemperature = scanner.nextDouble();

            double heartRate = heartRateCalculator.calculateHeartRate(bodyTemperature, physiologicalNormTemperature);
            System.out.println("Пульс: " + heartRate + " ударів на хвилину");

            System.out.println("Введіть параметр відображення (1 - Текстова таблиця, 2 - HTML Таблиця):");
            int displayOption = scanner.nextInt();
            CalculationResultRenderer renderer;
            if (displayOption == 1) {
                renderer = new TextTableResultRenderer();
            } else if (displayOption == 2) {
                renderer = new HTMLTableResultRenderer();
            } else {
                System.out.println("Недійсний параметр відображення. Будь ласка, виберіть 1 або 2.");
                return;
            }

            CalculationResult result = new CalculationResult(bodyTemperature, physiologicalNormTemperature);
            String renderedResult = renderer.render(result);
            System.out.println(renderedResult);

            parallelProcessingExample();
            workerThreadExample(bodyTemperature, physiologicalNormTemperature); // Передаємо параметри в метод
        }
    }

    private static void parallelProcessingExample() {
        System.out.println("Приклад паралельної обробки:");

        List<Double> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            data.add(Math.random() * 100);
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            Future<Double> minFuture = executor.submit(() -> data.stream().min(Double::compareTo).orElse(null));
            Future<Double> maxFuture = executor.submit(() -> data.stream().max(Double::compareTo).orElse(null));
            Future<Double> avgFuture = executor.submit(() -> data.stream().mapToDouble(Double::doubleValue).average().orElse(0));

            double min = minFuture.get();
            double max = maxFuture.get();
            double avg = avgFuture.get();

            System.out.println("Мінімум: " + min);
            System.out.println("Максимум: " + max);
            System.out.println("Середнє знач: " + avg);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void workerThreadExample(double bodyTemperature, double physiologicalNormTemperature) {
        System.out.println("Приклад робочого потоку:");

        BlockingQueue<Command> queue = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                while (true) {
                    Command command = queue.take();
                    command.undo();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        for (int i = 0; i < 10; i++) {
            queue.add(new HeartRateCalculationCommand(bodyTemperature, physiologicalNormTemperature)); // Передаємо параметри
        }

        executor.shutdown(); // Завершуємо роботу виконавчого потоку
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES); // Чекаємо закінчення роботи виконавчого потоку протягом 1 хвилини
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class HeartRateCalculationCommand implements Command {
    private double bodyTemperature;
    private double physiologicalNormTemperature;

    public HeartRateCalculationCommand(double bodyTemperature, double physiologicalNormTemperature) {
        this.bodyTemperature = bodyTemperature;
        this.physiologicalNormTemperature = physiologicalNormTemperature;
    }

    @Override
    public void undo() {
        // Реалізація скасування команди
    }
}

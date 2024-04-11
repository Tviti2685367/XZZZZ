import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) {
        HeartRateCalculator heartRateCalculator = HeartRateCalculator.getInstance();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter body temperature:");
            double bodyTemperature = scanner.nextDouble();

            System.out.println("Enter physiological norm temperature:");
            double physiologicalNormTemperature = scanner.nextDouble();

            double heartRate = heartRateCalculator.calculateHeartRate(bodyTemperature, physiologicalNormTemperature);
            System.out.println("Heart rate: " + heartRate + " beats per minute");

            System.out.println("Enter display option (1 - Text table, 2 - HTML table):");
            int displayOption = scanner.nextInt();
            CalculationResultRenderer renderer;
            if (displayOption == 1) {
                renderer = new TextTableResultRenderer();
            } else if (displayOption == 2) {
                renderer = new HTMLTableResultRenderer();
            } else {
                System.out.println("Invalid display option. Please choose 1 or 2.");
                return;
            }

            CalculationResult result = new CalculationResult(bodyTemperature, physiologicalNormTemperature);
            String renderedResult = renderer.render(result);
            System.out.println(renderedResult);

            parallelProcessingExample();
            workerThreadExample();
        }
    }

    private static void parallelProcessingExample() {
        System.out.println("Parallel processing example:");

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

            System.out.println("Min: " + min);
            System.out.println("Max: " + max);
            System.out.println("Average: " + avg);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void workerThreadExample() {
        System.out.println("Worker thread example:");

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
            queue.add(new HeartRateCalculationCommand());
        }

        executor.shutdown();
    }
}

class HeartRateCalculationCommand implements Command {
    @Override
    public void undo() {
        // Not implemented as this is an example
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReactorTemperatureMonitor {
    private static final int LAST_TEN_LIMIT = 10;
    private static final int HUNDRED_MEASUREMENT_INTERVAL = 100;
    private static final double AVERAGE_THRESHOLD_MULTIPLIER = 1.1;
    private static final double DIFFERENCE_THRESHOLD_MULTIPLIER = 0.2;

    private List<Double> allTemperatures;
    private List<Double> lastTenTemperatures;

    public ReactorTemperatureMonitor() {
        this.allTemperatures = new ArrayList<>();
        this.lastTenTemperatures = new ArrayList<>();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        double previousTemperature = 0;
        boolean isFirstTemperature = true;

        while (true) {
            System.out.print("Введите температуру ядерного реактора: ");
            double currentTemperature = scanner.nextDouble();
            double differenceFromPrevious = isFirstTemperature ? 0 : currentTemperature - previousTemperature;

            addTemperature(currentTemperature);

            double averageAllTime = calculateAverage(allTemperatures);

            System.out.printf("Средняя температура за все время: %.2f%n", averageAllTime);
            if (!isFirstTemperature) {
                System.out.printf("Разница с предыдущей температурой: %.2f%n", differenceFromPrevious);
            } else {
                System.out.println("Это первое введенное значение.");
                isFirstTemperature = false;
            }

            if (isTemperatureAlert(currentTemperature, averageAllTime, differenceFromPrevious, previousTemperature)) {
                System.out.println("Тревога, превышение температуры!");
            }

            double averageLastTen = calculateAverage(lastTenTemperatures);
            System.out.printf("Средняя температура за последние %d измерений: %.2f%n", LAST_TEN_LIMIT, averageLastTen);

            if (allTemperatures.size() % HUNDRED_MEASUREMENT_INTERVAL == 0 && allTemperatures.size() > 0) {
                double averageHundreds = calculateAverageEveryHundred();
                System.out.printf("Средняя температура каждого %d-го измерения: %.2f%n", HUNDRED_MEASUREMENT_INTERVAL, averageHundreds);
            }

            previousTemperature = currentTemperature;
        }
    }

    private void addTemperature(double currentTemperature) {
        allTemperatures.add(currentTemperature);

        if (lastTenTemperatures.size() >= LAST_TEN_LIMIT) {
            lastTenTemperatures.remove(0);
        }
        lastTenTemperatures.add(currentTemperature);
    }

    private double calculateAverage(List<Double> temperatures) {
        return temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private boolean isTemperatureAlert(double currentTemperature, double averageAllTime, double differenceFromPrevious, double previousTemperature) {
        return currentTemperature > averageAllTime * AVERAGE_THRESHOLD_MULTIPLIER &&
                differenceFromPrevious > previousTemperature * DIFFERENCE_THRESHOLD_MULTIPLIER;
    }

    private double calculateAverageEveryHundred() {
        double sum = 0;
        int count = 0;
        for (int i = HUNDRED_MEASUREMENT_INTERVAL - 1; i < allTemperatures.size(); i += HUNDRED_MEASUREMENT_INTERVAL) {
            sum += allTemperatures.get(i);
            count++;
        }
        return count == 0 ? 0 : sum / count;
    }
}

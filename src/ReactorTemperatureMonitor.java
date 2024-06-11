import java.util.ArrayList;
import java.util.Scanner;

public class ReactorTemperatureMonitor {
    private ArrayList<Double> allTemperatures;
    private ArrayList<Double> lastTenTemperatures;
    private double previousTemperature;
    private boolean isFirstTemperature;

    public ReactorTemperatureMonitor() {
        this.allTemperatures = new ArrayList<>();
        this.lastTenTemperatures = new ArrayList<>();
        this.previousTemperature = 0;
        this.isFirstTemperature = true;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

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
            }

            if (isTemperatureAlert(currentTemperature, averageAllTime, differenceFromPrevious)) {
                System.out.println("Тревога, превышение температуры!");
            }

            double averageLastTen = calculateAverage(lastTenTemperatures);
            System.out.printf("Средняя температура за последние 10 измерений: %.2f%n", averageLastTen);

            if (allTemperatures.size() % 100 == 0 && allTemperatures.size() > 0) {
                double averageHundreds = calculateAverageEveryHundred();
                System.out.printf("Средняя температура каждого 100-го измерения: %.2f%n", averageHundreds);
            }
        }
    }

    private void addTemperature(double currentTemperature) {
        allTemperatures.add(currentTemperature);

        if (lastTenTemperatures.size() >= 10) {
            lastTenTemperatures.remove(0);
        }
        lastTenTemperatures.add(currentTemperature);

        if (!isFirstTemperature) {
            previousTemperature = currentTemperature;
        } else {
            isFirstTemperature = false;
        }
    }

    private double calculateAverage(ArrayList<Double> temperatures) {
        return temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    private boolean isTemperatureAlert(double currentTemperature, double averageAllTime, double differenceFromPrevious) {
        return currentTemperature > averageAllTime * 1.1 && differenceFromPrevious > previousTemperature * 0.2;
    }

    private double calculateAverageEveryHundred() {
        double sum = 0;
        int count = 0;
        for (int i = 99; i < allTemperatures.size(); i += 100) {
            sum += allTemperatures.get(i);
            count++;
        }
        return count == 0 ? Double.NaN : sum / count;
    }
}

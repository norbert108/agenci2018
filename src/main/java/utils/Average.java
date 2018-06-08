package utils;

public class Average {
    public static double calculateAverage(int numArray[]) {
        int sum = 0;
        for (int d : numArray) sum += d;

        return 1.0d * sum / numArray.length;
    }
}

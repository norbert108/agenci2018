package utils;

import java.util.List;

public class Average {
    public static double calculateAverage(List<Integer> numArray) {
        int sum = 0;
        for (int d : numArray) sum += d;

        return 1.0d * sum / numArray.size();
    }
}

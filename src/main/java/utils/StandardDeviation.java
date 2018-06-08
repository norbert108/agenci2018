package utils;

import java.util.List;

public class StandardDeviation {
    public static double calculateSD(List<Integer> numArray) {
        double sum = 0.0, standardDeviation = 0.0;

        for(double num : numArray) { sum += num; }

        double mean = sum/10;

        for(double num: numArray) { standardDeviation += Math.pow(num - mean, 2); }

        return Math.sqrt(standardDeviation/10);
    }
}

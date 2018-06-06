package parser;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvFilesGenerator {

    private final String SEPARATOR = ";";

    @Test
    public void generate() throws IOException {

        List<String> countries = new ArrayList<>();
        countries.add("polska");
        countries.add("niemcy");
        countries.add("rosja");
        countries.add("izrael");
        countries.add("usa");
        countries.add("japonia");
        countries.add("francja");
        countries.add("wlochy");
        countries.add("hiszpania");
        countries.add("irlandia");
        countries.add("portugalia");
        countries.add("wegry");
        countries.add("czechy");
        countries.add("slowacja");
        countries.add("wielkaBrytania");
        countries.add("senegal");
        countries.add("kolumbia");
        countries.add("brazylia");

        for(int i = 0; i < 100; i++) {

            String content = "";
            for(int j = 0; j < 100; j++) {
                content += getRandomDate() + SEPARATOR;

                Integer tagsNumber = RandomUtils.nextInt(1, 4);
                Set<String> tags = new HashSet<>();
                while(tags.size() < tagsNumber) {
                    tags.add(countries.get(RandomUtils.nextInt(0, countries.size())));
                }

                for(String tag : tags) {
                    content += tag + SEPARATOR;
                }

                content += "\n";
            }

            String filename = "file"+i+".csv";
            Files.createFile(Paths.get("csv/" + filename));
            Files.write(Paths.get("csv/" + filename), content.getBytes());
        }
    }

    public String getRandomDate() {

        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(2005, 2017);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}

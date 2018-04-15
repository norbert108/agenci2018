import java.util.*;

import static java.lang.Math.abs;

public class TagGenerator {

    private List<String> countries;

    public TagGenerator() {
        countries = new ArrayList<String>();
        countries.add("pol");
        countries.add("rus");
        countries.add("uk");
        countries.add("usa");
    }

    public Set<String> getTags(int count) {
        Set<String> tags = new HashSet<String>();
        Random rand = new Random();
        for(int i = 0; i < count; i++) {
            int index = abs(rand.nextInt()) % countries.size();
            tags.add(countries.get(index));
        }
        return tags;
    }
}

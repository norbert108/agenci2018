import java.util.*;

import static java.lang.Math.abs;

public class TagGenerator {

    private List<String> countries;
    private List<Set<String>> predefinedTags = new ArrayList<>();
    private Integer requestNo = 0;

    public TagGenerator() {
        countries = new ArrayList<>();
        countries.add("pol");
        countries.add("rus");

        // zainicjalizuj predefiniowane tagi
        Set<String> tagset1 = new HashSet<>();
        tagset1.add("polska");
        tagset1.add("niemcy");
        tagset1.add("rosja");

        Set<String> tagset2 = new HashSet<>();
        tagset2.add("polska");
        tagset2.add("izrael");

        Set<String> tagset3 = new HashSet<>();
        tagset3.add("izrael");
        tagset3.add("usa");
        tagset3.add("uk");

        predefinedTags.add(tagset1);
        predefinedTags.add(tagset2);
        predefinedTags.add(tagset3);
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

    public Set<String> getTags() {
        requestNo++;
        return predefinedTags.get((requestNo-1)%3);
    }
}

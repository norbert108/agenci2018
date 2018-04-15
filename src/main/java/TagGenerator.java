import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TagGenerator {

    private List<String> countries;

    public TagGenerator() {
        countries = new ArrayList<String>();
        countries.add("pol");
        countries.add("rus");
//        countries.add("UK");
//        countries.add("USA");
    }

    public ArrayList<String> getTags(int count) {
        ArrayList<String> tags = new ArrayList<String>();
        Random rand = new Random();
        for(int i = 0; i < count; i++) {
            int index = rand.nextInt() % countries.size();
            tags.add(countries.get(index-1));
        }
        return tags;
    }
}

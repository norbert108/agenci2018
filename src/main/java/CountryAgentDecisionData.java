import java.util.HashMap;
import java.util.Map;

public class CountryAgentDecisionData {
    private Map<String, Integer> countriesCount = new HashMap<>();

    public void addCountry(String country, int count) {
        int singleCountryCount = this.countriesCount.get(country);

        this.countriesCount.put(country, singleCountryCount);
    }
}

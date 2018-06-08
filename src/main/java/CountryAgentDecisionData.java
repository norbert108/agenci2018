import java.util.HashMap;
import java.util.Map;

public class CountryAgentDecisionData {
    private int numberOfAddedTimes = 0;
    private final String country;
    private Map<String, Integer> countriesCount = new HashMap<>();

    public CountryAgentDecisionData(String country) {
        this.country = country;
    }

    public void addCountry(String country, int count) {
        int singleCountryCount = 0;

        if (this.countriesCount.containsKey(country)) {
            singleCountryCount = this.countriesCount.get(country);
        }

        this.countriesCount.put(country, singleCountryCount + count);
        numberOfAddedTimes++;
    }

    public Map<String, Integer> getCountriesCount() {
        return countriesCount;
    }

    public String getCountry() {
        return country;
    }

    public int getNumberOfAddedTimes() {
        return numberOfAddedTimes;
    }
}

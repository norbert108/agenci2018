import java.util.Date;
import java.util.Map;

public class HistoryRecord {
    private Date creationDate;
    private String sourceAgentName;
    private Map<String, Integer> occurencies;

    public HistoryRecord(Date creationDate, String sourceAgentName, Map<String, Integer> occurencies) {
        this.creationDate = creationDate;
        this.sourceAgentName = sourceAgentName;
        this.occurencies = occurencies;
    }

    @Override
    public String toString() {
        return creationDate + ":  --" + sourceAgentName + "--" + occurencies.toString();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getSourceAgentName() {
        return sourceAgentName;
    }

    public Map<String, Integer> getOccurencies() {
        return occurencies;
    }
}

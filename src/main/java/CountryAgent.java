import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import parser.CsvParser;

import java.util.*;


public class CountryAgent extends Agent {
    private String name;
    private Map<String, Integer> messageCounters = new HashMap<>();
    private List<HistoryRecord> history = new ArrayList<>();
    private TagGenerator tagGenerator = new TagGenerator();
    private CsvParser csvParser = new CsvParser();

    private TickerBehaviour analyzeNews = new TickerBehaviour(this, 2000) {
        @Override
        public void onTick() {
            // symuluje analize pojedynczego artykułu
            //Set<String> tags = tagGenerator.getTags();

            Set<String> tags = csvParser.getTagsFromRandomArticle();




            System.out.println("[" + name + "] read tags: " + tags.toString());

            Map<String, Integer> resultOccurrencies = new HashMap<>();
            for (String tag: tags) {
                Integer occurrencies = resultOccurrencies.get(tag);
                resultOccurrencies.put(tag, (occurrencies == null ? 0 : occurrencies) + 1);
            }

            // notify interested agents
            String messageContent = Utils.dateToString(new Date()) + "@" + resultOccurrencies.toString();
            for(String tag: tags) {
                if(tag.equals(name)) continue;
                if(Utils.createAgentIfNotExist(tag, myAgent)) {
                    send(Utils.prepareMsg("decision", "register_agent", tag));
                }
                send(Utils.prepareMsg(tag, "update", messageContent));
            }
        }
    };

    private CyclicBehaviour receiveMessages = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                String senderName = msg.getSender().getLocalName();
                String messageType = msg.getProtocol();
                String messageContent = msg.getContent();
                String[] contentStringArray = messageContent.split("@");
                printDebugInfo(senderName, messageContent);

                if(messageType.equals("update")) {
                    Date creationDate = Utils.stringToDate(contentStringArray[0]);
                    String contentString = contentStringArray[1];

                    // zdekoduj wiadomość
                    Map<String, Integer> contentMap = Utils.stringToMap(contentString);
                    history.add(new HistoryRecord(creationDate, senderName, contentMap));

                    // todo trzeba sortować dane po update!
                } else if(messageType.equals("query")) {
                    long queryId = Long.valueOf(contentStringArray[0]);
                    Date startDate = Utils.stringToDate(contentStringArray[1]);
                    Date endDate = Utils.stringToDate(contentStringArray[2]);

                    // find index range
                    Integer startIndex = 0;
                    Integer endIndex = history.size() - 1;
                    for(int i = 0; i < history.size(); i++) {
                        if(history.get(i).getCreationDate().after(startDate)) {
                            startIndex = i;
                            break;
                        }
                    }
                    for(int i = history.size() - 1; i >= 0; i--) {
                        if(history.get(i).getCreationDate().before(endDate)) {
                            endIndex = i;
                            break;
                        }
                    }

                    // parse data
                    System.out.println("query from " + senderName + ":  " + startDate.toString() + "  " + endDate.toString());
                    Map<String, Integer> summary = new HashMap<>();
                    for(int i = startIndex; i <= endIndex; i++) {
                        Map<String, Integer> occurrenciesEntry = history.get(i).getOccurencies();
                        for(String countryName: occurrenciesEntry.keySet()) {
                            Integer occurrencies = summary.get(countryName);
                            summary.put(countryName, (occurrencies == null ? 0 : occurrencies) + occurrenciesEntry.get(countryName));
                        }
                    }
                    String replyMsg = queryId + "@" + summary.toString();
                    send(Utils.prepareMsg(senderName, "query_reply", replyMsg));
                }
            }
            block();
        }

        private void printDebugInfo(String senderName, String content) {
            Integer messageCounter = messageCounters.get(senderName);
            messageCounters.put(senderName, (messageCounter == null ? 0 : messageCounter) + 1);
            System.out.println(messageCounters.get(senderName) + " - [" + name + "]: from " + senderName + " <- " + content);
        }
    };

    protected void setup() {
        this.name = getLocalName();

        addBehaviour(analyzeNews);
        addBehaviour(receiveMessages);
    }
}
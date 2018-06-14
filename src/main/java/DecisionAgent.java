import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Average;
import utils.StandardDeviation;

import java.util.*;
import java.util.logging.Logger;


public class DecisionAgent extends Agent {
    private final static Logger LOGGER = Logger.getLogger(DecisionAgent.class.getName());
    private final static int MIN_NUMBER_TO_ANALYZE = 30;
    private List<String> subscribedAgents = new ArrayList<>();
    private Map<String, CountryAgentDecisionData> countryAgentDecisionDataMap = new HashMap<>();

    private TickerBehaviour queryPeriodically = new TickerBehaviour(this, 2000) {
        public void onTick() {
            for(String agent: subscribedAgents) {
                Date startDate = Utils.addToDate(new Date(), Calendar.SECOND, -10);
                Date endDate = Utils.addToDate(new Date(), Calendar.SECOND, -5);
                String queryId = String.valueOf(System.currentTimeMillis());

                String messageContent = queryId + "@" + Utils.dateToString(startDate) + "@" +  Utils.dateToString(endDate);
                send(Utils.prepareMsg(agent, "query", messageContent));
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

                if (messageType.equals("query_reply")) {
                    System.out.println("!!! query_reply: " + messageContent);
                    System.out.println("!!! sendername: " + senderName);

                    saveAndAnalyze(senderName, messageContent);

                    // todo tutaj analiza danych i decyzje różne
                    // todo tworzenie agentów obsługujących wiele krajów należy do tego agenta
                } if (messageType.equals("register_agent")) {
                    subscribedAgents.add(messageContent);
                }
            }
        }
    };

    private void saveAndAnalyze(String senderName, String messageContent) {
        Map<String, Integer> parsedContent = Utils.stringToMap(messageContent);
        CountryAgentDecisionData existingData;

        if (countryAgentDecisionDataMap.containsKey(senderName)) {
            existingData = countryAgentDecisionDataMap.get(senderName);
        } else {
            existingData = new CountryAgentDecisionData(senderName);
        }

        for (Map.Entry<String, Integer> entry : parsedContent.entrySet()) {
            existingData.addCountry(entry.getKey(), entry.getValue());
        }

        countryAgentDecisionDataMap.put(senderName, existingData);

        if (existingData.getNumberOfAddedTimes() > MIN_NUMBER_TO_ANALYZE) {
            analyze(existingData);
        } else {
            LOGGER.info("Omiting country for now");
        }
    }

    private void analyze(CountryAgentDecisionData existingData) {
        analyzeConnections(existingData);
        analyzeMessagesCounts(existingData);
    }

    private void analyzeMessagesCounts(CountryAgentDecisionData existingData) {
        ArrayList<Integer> messagesInCountryList = new ArrayList<>();

        for (Map.Entry<String, CountryAgentDecisionData> entry : countryAgentDecisionDataMap.entrySet()) {
            int countryTotalCount = 0;
            for (Map.Entry<String, Integer> countriesCountEntry : entry.getValue().getCountriesCount().entrySet()) {
                countryTotalCount =+ countriesCountEntry.getValue();
            }
            messagesInCountryList.add(countryTotalCount);
        }

        double stdev = StandardDeviation.calculateSD(messagesInCountryList);
        double average = Average.calculateAverage(messagesInCountryList);
        LOGGER.info("MESSAGESANALYZER " + existingData.getCountry() + " messages count, stdev: " + stdev + " avg: " + average);

        int currentCountryCount = 0;
        for (Map.Entry<String, Integer> entry : existingData.getCountriesCount().entrySet()) {
            currentCountryCount += entry.getValue();
        }

        String messageToNotify = null;
        if (currentCountryCount > average + stdev) {
            messageToNotify = existingData.getCountry() + " agent has many messages (" + currentCountryCount + ")!";
        } else if (currentCountryCount < average - stdev) {
            messageToNotify = existingData.getCountry() + " agent has low messages (" + currentCountryCount + ")!";
        }

        if (messageToNotify != null) {
            this.sendNotification(messageToNotify);
        }
    }

    private void analyzeConnections(CountryAgentDecisionData existingData) {
        for (Map.Entry<String, Integer> entry : existingData.getCountriesCount().entrySet()) {
            String country = entry.getKey();
            ArrayList<Integer> countryCountList = new ArrayList<>();

            for (Map.Entry<String, CountryAgentDecisionData> countryAgentDecisionDataEntry: countryAgentDecisionDataMap.entrySet()) {
                Map<String, Integer> countriesCount = countryAgentDecisionDataEntry.getValue().getCountriesCount();
                if (countriesCount.containsKey(country)) {
                    countryCountList.add(countriesCount.get(country));
                }
            }

            double stdev = StandardDeviation.calculateSD(countryCountList);
            double average = Average.calculateAverage(countryCountList);
            LOGGER.info(existingData.getCountry() + " with " + country + " stdev: " + stdev + " avg: " + average);
            Integer checkedDataCountryCount = existingData.getCountriesCount().get(country);

            String messageToNotify = null;

            if (checkedDataCountryCount > average + stdev) {
                messageToNotify = existingData.getCountry() + " agent notifies about many connections (" + checkedDataCountryCount + ") to " + country;
            } else if (checkedDataCountryCount < average - stdev) {
                messageToNotify = existingData.getCountry() + " agent notifies about low number of connections (" + checkedDataCountryCount + ") to " + country;
            }

            if (messageToNotify != null) {
                this.sendNotification(messageToNotify);
            }
        }
    }

    private void sendNotification(String messageToNotify) {
        send(Utils.prepareMsg("notifier", "notification", messageToNotify));
    }

    protected void setup() {
        addBehaviour(receiveMessages);
        addBehaviour(queryPeriodically);
    }
}

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.*;


public class DecisionAgent extends Agent {
    private List<String> subscribedAgents = new ArrayList<>();

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
                    System.out.println("query_reply: " + messageContent);

                    // todo tutaj analiza danych i decyzje różne
                    // todo tworzenie agentów obsługujących wiele krajów należy do tego agenta
                } if (messageType.equals("register_agent")) {
                    subscribedAgents.add(messageContent);
                }
            }
        }
    };

    protected void setup() {
        addBehaviour(receiveMessages);
        addBehaviour(queryPeriodically);
    }
}

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountryAgent extends Agent {
    private String name;
    private Map<String, Integer> countryOccurrences;

    private CyclicBehaviour analyzeNews = new CyclicBehaviour() {
        @Override
        public void action() {
//            String name = getLocalName();
            // symuluje analize pojedynczego artykolu
            TagGenerator tagGenerator = new TagGenerator();
            Set<String> tags = tagGenerator.getTags(10);
            if (tags.contains(name)) {
                tags.remove(name); // usun polske

                Integer occurrencies = countryOccurrences.get(name);
                countryOccurrences.put(name, (occurrencies == null ? 0 : occurrencies) + 1);
                for (String tag : tags) {
                    sendMessage(tag, countryOccurrences); // wyslij do kazdego z zainteresowanych agentow
                    sendMessage("history", countryOccurrences);
//                    sendMessage("history", countryOccurrences.toString());
                }
            }
        }

        void sendMessage(String receiver, Map<String, Integer> content) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent(content.toString());
            msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            send(msg);
        }
    };

    private CyclicBehaviour receiveMessages = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println(" - " + myAgent.getLocalName() + " from " + msg.getSender() + " <- " + msg.getContent());
            }
            block();
        }
    };

    protected void setup() {
        this.name = getLocalName();
        this.countryOccurrences = new HashMap<String, Integer>();

        addBehaviour(analyzeNews);
        addBehaviour(receiveMessages);
    }
}
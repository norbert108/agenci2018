//   HelloAgent:    our first JADE program
//
//   Usage:    % javac HelloAgent.java
//             % java jade.Boot fred:HelloAgent
// --------------------------------------------------------

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class CountryAgent extends Agent
{
    private String name;
    private int articles;

    public CountryAgent() {
        this.name = getLocalName();
    }

    protected void setup()
    {
        addBehaviour(new SimpleBehaviour() {
            private CountryAgent helloAgent = (CountryAgent)myAgent;

            @Override
            public void action() {
                TagGenerator tagGenerator = new TagGenerator();
                ArrayList<String> tags = tagGenerator.getTags(10);
                if(tags.contains(name)) {
                    tags.remove(name);
                    helloAgent.articles++;
                    for(String tag: tags) {
                        sendMessage(tag, "Ty chuju!");
                    }
                }
            }

            @Override
            public boolean done() {
                return false;
            }

            public void sendMessage(String receiver, String content) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent(content);
                msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
                send(msg);
            }
        });

        addBehaviour(
                new CyclicBehaviour(this) {
                    public void action() {
                        ACLMessage msg = receive();
                        if (msg!=null) {
                            System.out.println(" - " + myAgent.getLocalName() + " <- " + msg.getContent());
                        }
                        block();
                    }
                }
        );
    }
}
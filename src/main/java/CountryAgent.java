import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class CountryAgent extends Agent {
    protected void setup() {

        addBehaviour(
                new SimpleBehaviour(this) {
                    public void action() {
                        ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
                        msg.setContent("I sell seashells at $10/kg" );
                        msg.addReceiver(new AID("history", AID.ISLOCALNAME));
                        send(msg);
                    }

                    public boolean done() {
                        return false;
                    }
                }
        );
    }
}
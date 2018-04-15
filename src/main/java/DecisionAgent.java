import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class DecisionAgent extends Agent {
    protected void setup() {
        addBehaviour(
                new SimpleBehaviour(this) {
                    public void action() {
                        System.out.println("Running decision " + myAgent.getLocalName());
                    }

                    public boolean done() {
                        return false;
                    }
                }
        );
    }
}

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import notifier.EmailSender;

import java.util.logging.Logger;

public class NotifierAgent extends Agent {
    private final static Logger LOGGER = Logger.getLogger(NotifierAgent.class.getName());
    private static String NOTIFICATION_EMAIL_RECIPIENT = System.getenv("NOTIFICATION_EMAIL_RECIPIENT");

    private CyclicBehaviour cyclicBehaviour = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                String messageType = msg.getProtocol();
                if (messageType.equals("notification")) { this.fireNotification(msg.getContent()); }
            }
        }

        private void fireNotification(String content) {
            LOGGER.info("[notification] " + content);
            if (NOTIFICATION_EMAIL_RECIPIENT != null && !NOTIFICATION_EMAIL_RECIPIENT.equals("")) {
                EmailSender.sendFromGMail(
                        new String[]{NOTIFICATION_EMAIL_RECIPIENT},
                        "Agent system notification!",
                        content
                );
            }
        }
    };

    @Override
    protected void setup() {
        addBehaviour(this.cyclicBehaviour);
    }
}

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.*;

public class Utils {

    public static ACLMessage prepareMsg(String receiver, String protocol, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(content);
        msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        msg.setProtocol(protocol);
        return msg;
    }

    public static String dateToString(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        return String.format("%d:%d:%d:%d:%d:%d", year, month, day, hour, minute, second);
    }

    public static Date stringToDate(String string) {
        Calendar cal = Calendar.getInstance();
        Integer[] date = Arrays.stream(string.split(":"))
                .map(s -> Integer.valueOf(s)).toArray(Integer[]::new);
        cal.set(date[0],date[1],date[2],date[3],date[4],date[5]);
        return cal.getTime();
    }

    public static Date addToDate(Date date, int type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(type, amount);
        return cal.getTime();
    }

    public static Map<String, Integer> stringToMap(String contentString) {
        Map<String, Integer> contentMap = new HashMap<>();
        String[] contentStringArray = contentString.substring(1, contentString.length() - 1).split(",");
        for (String element : contentStringArray) {
            String elements[] = element.split("=");

            Integer occurrencies = contentMap.get(elements[0]);
            contentMap.put(elements[0], (occurrencies == null ? 0 : occurrencies) + Integer.valueOf(elements[1]));
        }
        return contentMap;
    }

    public static Boolean createAgentIfNotExist(String name, Agent sourceAgent) {
        try {
            sourceAgent.getContainerController().getAgent(name);
        } catch (ControllerException e) {
            try {
                sourceAgent.getContainerController().createNewAgent(name, "CountryAgent", null).start();
            } catch (StaleProxyException e2) {
                e2.printStackTrace();
                return false;
            }
            return true;
        }

        return false;
    }
}

import java.util.HashSet;
import java.util.HashMap;


public class Ping implements Module {
	
	private HashSet<String> pings = new HashSet<String>();
	private HashMap<String, String> targets = new HashMap<String, String>();
	private Message m;
	private boolean admin;
	
	@Override
	public String[] outputs() {
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.getBotCommand().equals("ping")){
			if(m.hasBotParams()){
				String[] outputs = new String[m.getBotParams().size()];
				for(int i = 0; i < m.getBotParams().size(); i++){
					outputs[i] = "PRIVMSG " + m.getBotParams().get(i) + " :PING " + System.currentTimeMillis() + "";
					pings.add(m.getBotParams().get(i));
					targets.put(m.getBotParams().get(i), target);
				}
				return outputs;
			}
		}
		if(m.getCommand().equals("NOTICE")){
			
			if(m.getTrailing().substring(0, 5).equals("PING") && pings.contains(m.getSender())){
				String[] outputs = new String[1];
				long ms = System.currentTimeMillis() - Long.parseLong(m.getTrailing().substring(6,m.getTrailing().length()-1));
				outputs[0] = targets.get(m.getSender()) + " :Ping for " + m.getSender() + " :" + ms + "ms";
				pings.remove(m.getSender());
				targets.remove(m.getSender());
				return outputs;
			}
		}
		if(m.getCommand().equals("PING")){
			String[] outputs = new String[1];
			outputs[0] = "PONG :" + m.getTrailing();
			return outputs;
		}
		if(m.getTrailing().startsWith("PING")){
			String[] outputs = new String[1];
			outputs[0] = "NOTICE " + m.getSender() + " :" + m.getTrailing();
			return outputs;
		}
		return null;
	}

	@Override
	public void parse(Message message, boolean isadmin) {
		m = message;
		admin = isadmin;

	}

}

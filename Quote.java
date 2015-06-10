import java.util.ArrayList;
import java.util.HashMap;


public class Quote implements Module {
	
	private boolean admin;
	private Message m;
	private HashMap<String, ArrayList<String>> quotes;
	
	public Quote(){
		quotes = new HashMap<String, ArrayList<String>>();
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!target.startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.isBotCommand()){
			if(m.getBotCommand().equals("quote")){
				if(m.hasBotParams()){
					String user = m.getBotParams().get(0);
					if(quotes.get(user) != null){
						int random = (int)Math.floor(Math.random() * quotes.get(user).size());
						outputs[0] = target + " :<" + user + "> " + quotes.get(user).get(random);
						return outputs;
					}
				}
			}
		}
		else if(m.getCommand().equals("PRIVMSG")){
			if(quotes.get(m.getSender()) == null){
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(m.getTrailing());
				quotes.put(m.getSender(), temp);
			}
			else{
				ArrayList<String> temp = quotes.get(m.getSender());
				temp.add(m.getTrailing());
				quotes.put(m.getSender(), temp);
			}
		}
		return null;
	}

	@Override
	public void parse(Message message, boolean isadmin) {
		m = message;
		admin = isadmin;
	}

}

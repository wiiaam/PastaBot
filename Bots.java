import java.util.HashSet;


public class Bots implements Module{
	
	private String command = "bots";
	private Message m;
	private boolean admin;


	@Override
	public String[] outputs() {
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				String[] outputs = new String[1];
				String target = m.getParam();
				if(!target.startsWith("#")) target = m.getSender();
				outputs[0] = "PRIVMSG " + target + " :Reporting in! [Java]";
				return outputs;
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

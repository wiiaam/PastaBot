
public class Bots implements Module{
	
	private String command = "bots";
	private Message m;
	private boolean admin;


	@Override
	public String[] outputs() {
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				String[] outputs = new String[1];
				String target = "PRIVMSG " + m.getParam();
				if(!target.startsWith("#")) target = "NOTICE " + m.getSender();
				outputs[0] = target + " :Reporting in! [Java] See .help";
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

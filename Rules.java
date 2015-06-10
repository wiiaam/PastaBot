
public class Rules implements Module {

	private String command = "rules";
	private Message m;
	private boolean admin;

	@Override
	public String[] outputs() {
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				String[] outputs  = new String[3];
				outputs[0] = target + " :Rule 4: Bots must follow IBIP, this can be found at https://github.com/Teknikode/IBIP";
				outputs[1] = target + " :Rule 2: installgen2 is a cuck";
				outputs[2] = target + " :Rule 0: No australians";
				
				/*
				String[] outputs  = new String[5];
				outputs[0] = target + " :Rule 1: No Spamming";
				outputs[1] = target + " :Rule 2: Tag all NSFW links";
				outputs[2] = target + " :Rule 3: Bots must follow IBIP, this can be found at https://github.com/Teknikode/IBIP";
				outputs[3] = target + " :Rule 4: installgen2 is a cuck";
				outputs[4] = target + " :Rule 5: No australians";
				*/
				
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


public class Rules implements Module {

	private String command = "rules";
	private Message m;
	private boolean admin;

	@Override
	public String[] outputs() {
		String target = m.getParam();
		if(!target.startsWith("#")) target = m.getSender();
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				System.out.println(".rules");
				String[] outputs  = new String[4];
				outputs[0] = "PRIVMSG " + target + " :Rule 1: No Spamming";
				outputs[1] = "PRIVMSG " + target + " :Rule 2: Tag all NSFW links";
				outputs[2] = "PRIVMSG " + target + " :Rule 3: Bots must follow IBIP, this can be found at https://github.com/Teknikode/IBIP";
				outputs[3] = "PRIVMSG " + target + " :Rule 4: installgen2 is a cuck";
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

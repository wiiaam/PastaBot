
public class Help implements Module {

	private Message m;
	private boolean admin;
	
	
	@Override
	public String[] outputs() {
		if(m.isBotCommand()){
			if(m.getBotCommand().equals("help")){
				String[] outputs = new String[1];
				outputs[0] = "NOTICE " + m.getSender() + " :Commands can be found at bit.ly/1G0UB0b";
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

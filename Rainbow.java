
public class Rainbow implements Module {

	private Message m;
	private boolean admin;
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		
		if(m.getBotCommand().equals("rb") && m.hasBotParams()){
			String message = m.getBotParamsString();
			char[] chars = message.toCharArray();
			message = target + " :";
			int sequence = 0;
			for(int i = 0; i < chars.length; i++){
				if(i == 7*sequence + 0) message += "20" + chars[i];
				if(i == 7*sequence + 1) message += "23" + chars[i];
				if(i == 7*sequence + 2) message += "24" + chars[i];
				if(i == 7*sequence + 3) message += "25" + chars[i];
				if(i == 7*sequence + 4) message += "12" + chars[i];
				if(i == 7*sequence + 5) message += "22" + chars[i];
				if(i == 7*sequence + 6) {
					message += "13" + chars[i];
					sequence++;
				}
			}
			outputs[0] = message;
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

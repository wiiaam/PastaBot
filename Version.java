import java.util.HashMap;


public class Version implements Module {

	private Message m;
	private boolean admin;
	private HashMap<String, String> requests;
	
	public Version(){
		requests = new HashMap<String, String>();
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.getTrailing().equals("VERSION")){
			outputs[0] = "NOTICE " + target + " :VERSION PastaBot 4.20 by Java™ Enterprises";
			return outputs;
		}
		if(m.getCommand().equals("PRIVMSG")){
			if(m.isBotCommand()){
				if(m.getBotCommand().equals("version") || m.getBotCommand().equals("ver")){
					if(m.hasBotParams()){
						outputs = new String[m.getBotParams().size()];
						for(int i = 0; i < m.getBotParams().size(); i++){
							outputs[i] = "PRIVMSG " + m.getBotParams().get(i) + " :VERSION";
							requests.put(m.getBotParams().get(i), target);
						}
						return outputs;
					}
				}
			}
		}
		if(m.getCommand().equals("NOTICE")){
			if(requests.containsKey(m.getSender())){
				if(m.getTrailing().startsWith("VERSION")){
					String version = m.getTrailing().substring(8,m.getTrailing().length()-1);
					version = version.replace("WeeChat", "WeebChat");
					version = version.replace("weechat", "weebchat");
					version = version.replace("Weechat", "Weebchat");
					outputs[0] = requests.get(m.getSender()) +  " :[" + m.getSender() + "] Version: " + version;
					requests.remove(m.getSender());
					return outputs;
				}
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

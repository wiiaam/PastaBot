
public class Administration implements Module {

	private Message m;
	private boolean admin;
	@Override
	public String[] outputs() {
		if(!admin)return null;
		String[] outputs;
		if(m.getBotCommand().equals("kb")){
			if(m.hasBotParams()){
				String target = "PRIVMSG " + m.getParam();
				if(!m.getParam().startsWith("#")) {
					target = "NOTICE " + m.getSender();
					if(m.getBotParams().size() < 2){
						outputs = new String[1];
						outputs[1] = target + " :Not enough parameters. Usage: .kb <room> <users>";
					}
					else{
						outputs = new String[2];
						String reason = "";
						for(int i = 2; i < m.getBotParams().size(); i++){
							
						}
						outputs[1] = "KICK " + m.getParam() + " :" + m.getBotParams().get(0);
						outputs[0] = "MODE " + m.getParam() + " +b " + m.getBotParams().get(0);
					}
				}
				else{
					outputs = new String[m.getBotParams().size()*2];
					for(int i = 0; i < m.getBotParams().size(); i++){	
						outputs[i] = "KICK " + m.getParam() + " :" + m.getBotParams().get(i);
						outputs[i + m.getBotParams().size()] = "MODE " + m.getParam() + " +b " + m.getBotParams().get(i);
					}
				}
				return outputs;
			}
		}
		if(m.getBotCommand().equals("ub")){
			if(m.hasBotParams()){
				String target = m.getParam();
				if(!target.startsWith("#")){
					target = m.getSender();
					if(m.getBotParams().size() < 2){
						outputs = new String[1];
						outputs[1] = target + " :Not enough parameters. Usage: .ub <room> <users>";
					}
					else{
						outputs = new String[(m.getBotParams().size() - 1 )];
						for(int i = 1; i < m.getBotParams().size(); i++){	
							outputs[i-1] = "MODE " + m.getParam() + " -b " + m.getBotParams().get(i);
						}
					}
				}
				else{
					outputs = new String[m.getBotParams().size()];
					for(int i = 0; i < m.getBotParams().size(); i++){	
						outputs[i] = "MODE " + m.getParam() + " -b " + m.getBotParams().get(i);
					}
				}
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

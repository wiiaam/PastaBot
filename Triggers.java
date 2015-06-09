import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


public class Triggers implements Module {

	private File file;
	private HashMap<String, String> triggers;
	private Message m;
	private boolean admin;
	
	public Triggers(){
		try {
			this.file = new File(this.getClass().getResource("triggers").toURI());
		} 
		catch (URISyntaxException e) {}
		triggers = new HashMap<String, String>();
		Scanner scan;
		try {
			scan = new Scanner(file);
			while(scan.hasNext()){
				String next = scan.nextLine();
				String key = next.split(" : ")[0];
				String value = next.split(" : ")[1];
				triggers.put(key, value);
			}
		} 
		catch (FileNotFoundException e) {
		}
	}
	
	public void write(){
		try {
			PrintWriter writer = new PrintWriter(file);
			for(Entry<String, String> entry : triggers.entrySet()){
				String s = entry.getKey() + " : " + entry.getValue();
				writer.println(s);
			}
			writer.close();
		} 
		catch (IOException e) {}
		
	}

	public boolean has(String trigger){
		return triggers.containsKey(trigger);
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = m.getParam();
		if(!target.startsWith("#")) target = m.getSender();
		if(m.getBotCommand().equals("trigger")){
			if(admin){
				if(m.hasBotParams()){
					if(m.getBotParamsString().split(" : ").length > 1){
						String key = m.getBotParamsString().split(" : ")[0];
						String value = m.getBotParamsString().split(" : ")[1];
						triggers.put(key, value);
						write();
						if (triggers.size() == 1) outputs[0] = "PRIVMSG " + target + " :Trigger added. There is now " + triggers.size() + " trigger set.";
						else outputs[0] = "PRIVMSG " + target + " :Trigger added. There is now " + triggers.size() + " triggers set.";
						return outputs;
					}
					else{
						outputs[0] = "PRIVMSG " + target + " :Not enough parameters specified";
						return outputs;
					}
				}
				else{
					outputs[0] = "PRIVMSG " + target + " :Not enough parameters specified";
					return outputs;
				}
			}
		}
		if(m.getBotCommand().equals("untrigger")){
			if(admin){
				if(m.hasBotParams()){
					if(triggers.containsKey(m.getBotParamsString())){
						triggers.remove(m.getBotParamsString());
						write();
						if (triggers.size() == 1) outputs[0] = "PRIVMSG " + target + " :Trigger removed. There is now " + triggers.size() + " trigger set.";
						else outputs[0] = "PRIVMSG " + target + " :Trigger removed. There is now " + triggers.size() + " triggers set.";
					}
					else{
						if (triggers.size() == 1) outputs[0] = "PRIVMSG " + target + " :Trigger not found. There is still " + triggers.size() + " trigger set.";
						else outputs[0] = "PRIVMSG " + target + " :Trigger not found. There is still " + triggers.size() + " triggers set.";
					}
					return outputs;
				}
				else{
					outputs[0] = "PRIVMSG " + target + " :No trigger was specified";
					return outputs;
				}
			}
		}
		if(m.getBotCommand().equals("listtriggers")){
			if(triggers.size() == 0){
				outputs = new String[1];
				outputs[0] = "There are no triggers";
				return outputs;
			}
			outputs = new String[triggers.size() + 1];
			if (triggers.size() == 1) outputs[0] = "PRIVMSG " + target + " :There is " + triggers.size() + " trigger set.";
			else outputs[0] = "PRIVMSG " + target + " :There is " + triggers.size() + " triggers set.";
			int i = 1;
			for(Entry<String, String> entry : triggers.entrySet()){
				outputs[i] = "PRIVMSG " + target + " :" + entry.getKey() + " : " + entry.getValue();
				i++;
			}
			return outputs;
		}
		if(m.getCommand().equals("PRIVMSG")){
			String[] trailingSplit = m.getTrailing().split("\\s+");
			if(trailingSplit.length > 50) return null;
			for(Entry<String, String> entry : triggers.entrySet()){
				if(m.getTrailing().toLowerCase().contains(entry.getKey())){
					String message = entry.getValue();
					message = message.replace("&sender", m.getSender());
					outputs[0] = "PRIVMSG " + target + " :" + message;
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

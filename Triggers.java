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
				String key = next.split("\\s+")[0];
				String value = "";
				for(int i = 1; i < next.split("\\s+").length; i++){
					value += next.split("\\s+")[i] + " ";
				}
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
				String s = entry.getKey() + " " + entry.getValue();
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
					if(m.getBotParams().size() > 1){
						String key = m.getBotParams().get(0);
						String value = "";
						for(int i = 1; i < m.getBotParams().size(); i++){
							value += m.getBotParams().get(i) + " ";
						}
						triggers.put(key, value);
						write();
						outputs[0] = "PRIVMSG " + target + " :The specified triggers have been added";
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
					for(int i = 0; i < m.getBotParams().size(); i++){
						triggers.remove(m.getBotParams().get(i));
					}
					write();
					outputs[0] = "PRIVMSG " + target + " :The specified triggers have been removed";
					return outputs;
				}
				else{
					outputs[0] = "PRIVMSG " + target + " :No users were specified";
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
			outputs = new String[triggers.size()];
			int i = 0;
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
					outputs[0] = "PRIVMSG " + target + " :" + entry.getValue();
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

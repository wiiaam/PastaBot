import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;


public class Ignores implements Module {

	private File file;
	private HashSet<String> ignores;
	private String command = "ignore";
	private Message m;
	private boolean admin;
	
	public Ignores(){
		try {
			this.file = new File(this.getClass().getResource("ignores").toURI());
		} 
		catch (URISyntaxException e) {}
		ignores = new HashSet<String>();
		Scanner scan;
		try {
			scan = new Scanner(file);
			while(scan.hasNext()){
				ignores.add(scan.nextLine());
			}
		} 
		catch (FileNotFoundException e) {
		}
	}
	
	public void write(){
		try {
			PrintWriter writer = new PrintWriter(file);
			for(String s : ignores){
				writer.println(s);
			}
			writer.close();
		} 
		catch (IOException e) {}
		
	}

	public boolean has(String username){
		return ignores.contains(username);
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!target.startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.isBotCommand()){
			if(m.getBotCommand().equals("ignore")){
				if(admin){
					if(m.hasBotParams()){
						for(int i = 0; i < m.getBotParams().size(); i++){
							ignores.add(m.getBotParams().get(i));
						}
						write();
						outputs[0] = target + " :The specified users have been ignored";
						return outputs;
					}
					else{
						outputs[0] = target + " :No users were specified";
						return outputs;
					}
				}
			}
			if(m.getBotCommand().equals("unignore")){
				if(admin){
					if(m.hasBotParams()){
						for(int i = 0; i < m.getBotParams().size(); i++){
							ignores.remove(m.getBotParams().get(i));
						}
						write();
						outputs[0] = target + " :The specified users have been unignored";
						return outputs;
					}
					else{
						outputs[0] = target + " :No users were specified";
						return outputs;
					}
				}
			}
			if(m.getBotCommand().equals("listignores")){
				outputs[0] = "";
				for(String s : ignores){
					outputs[0] += s + ", ";
				}
				if(outputs[0].length() > 0){
					outputs[0] = outputs[0].substring(0, outputs[0].length() - 2);
				}
				else{
					outputs[0] = "No users are currently ignored";
				}
				outputs[0] = target + " :" + outputs[0];
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

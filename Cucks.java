import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;


public class Cucks implements Module {

	private File file;
	private HashSet<String> Cucks;
	private String command = "cuck";
	private Message m;
	private boolean admin;
	
	public Cucks(){
		try {
			this.file = new File(this.getClass().getResource("cucks").toURI());
		} 
		catch (URISyntaxException e) {}
		Cucks = new HashSet<String>();
		Scanner scan;
		try {
			scan = new Scanner(file);
			while(scan.hasNext()){
				Cucks.add(scan.nextLine());
			}
		} 
		catch (FileNotFoundException e) {
		}
	}
	
	public void write(){
		try {
			PrintWriter writer = new PrintWriter(file);
			for(String s : Cucks){
				writer.println(s);
			}
			writer.close();
		} 
		catch (IOException e) {}
		
	}

	public boolean has(String username){
		return Cucks.contains(username);
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!target.startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.isBotCommand() && m.getBotCommand().equals("cuck")){
			if(admin){
				if(m.hasBotParams()){
					for(int i = 0; i < m.getBotParams().size(); i++){
						if(m.getBotParams().get(i).equals("PastaBot")){
							outputs = new String[2];
							outputs[1] = target + " :You can't cuck me. Nice try 1,1big guy";
						}
						else{
							Cucks.add(m.getBotParams().get(i));
						}
					}
					write();
					outputs[0] = target + " :The specified users have been cucked";
					return outputs;
				}
				else{
					outputs[0] = target + " :No users were specified";
					return outputs;
				}
			}
		}
		if(m.getBotCommand().equals("uncuck")){
			if(admin){
				if(m.hasBotParams()){
					for(int i = 0; i < m.getBotParams().size(); i++){
						Cucks.remove(m.getBotParams().get(i));
					}
					write();
					outputs[0] = target + " :The specified users have been uncucked";
					return outputs;
				}
				else{
					outputs[0] = target + " :No users were specified";
					return outputs;
				}
			}
		}
		if(m.getBotCommand().equals("listcucks")){
			outputs[0] = "";
			for(String s : Cucks){
				outputs[0] += s + ", ";
			}
			if(outputs[0].length() > 0){
				outputs[0] = outputs[0].substring(0, outputs[0].length() - 2);
			}
			else{
				outputs[0] = "No users are currently cucked";
			}
			outputs[0] = target + " :" + outputs[0];
			return outputs;
		}
		if(m.getCommand().equals("PRIVMSG") && has(m.getSender())){
			outputs = new String[2];
			outputs[0] = target + " :" + m.getSender() + ": shutup cuck";
			outputs[1] = "KICK " + m.getParam() + " " + m.getSender() + " :cuck"; 
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

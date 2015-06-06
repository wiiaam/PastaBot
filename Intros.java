import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;


public class Intros implements Module {
	
	private String command = "intro";
	private Message m;
	private boolean admin;
	private Properties props;
	
	public Intros(){
		read();
	}

	@Override
	public String[] outputs() {
		String[] outputs  = new String[1];
		if(m.getCommand().equals("JOIN")){
			if(props.getProperty(m.getSender()) != null && !props.getProperty(m.getSender()).equals("")){
				String intro = props.getProperty(m.getSender());
				outputs[0] = "PRIVMSG " + m.getTrailing() + " :" + intro ;
				return outputs;
			}
		}
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				String target = m.getParam();
				if(!target.startsWith("#")) target = m.getSender();
				String todo = "";
				String intro = "";
				if(!m.hasBotParams()){}
				else if(m.getSender().equals("installgen2") || m.getSender().equals("installgen2phone")){
					todo = "no";
				}
				else if(m.getBotParams().get(0).equals("set")){
					if(m.getBotParams().size() > 1){
						todo = "set";
						for(int i = 1; i <  m.getBotParams().size(); i++){
							intro += m.getBotParams().get(i) + " ";
						}
						if(intro.equals("")){
							todo = "";
						}
						props.setProperty(m.getSender(), intro);
					}
				}
				else if(m.getBotParams().get(0).equals("del")){
					todo = "deleted";
					if(props.getProperty(m.getSender()) != null){
						props.setProperty(m.getSender(), "");
					}
					else{
						todo = "nointro";
					}
				}
				
				
				try {
					FileOutputStream out = new FileOutputStream(new File(this.getClass().getResource("intros.properties").toURI()));
					props.store(out, "Intros");
					out.close();
				} 
				catch (IOException | URISyntaxException e) {}
				
				if(todo.equals("")){
					String[] toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :Parameter not recognized. Usage: .intro <parameter> <intro> Parameters are set, del ";
					return toreturn;
				}
				if(todo.equals("nointro")){
					String[] toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :You do not have an intro set";
					return toreturn;
				}
				if(todo.equals("no")){
					String[] toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :fk u";
					return toreturn;
				}
				else{
					String[] toreturn;
					toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :Your intro has been " + todo;
					return toreturn;
				}
			}
		}
		return null;
	}
	
	public void read(){
		InputStream stream = getClass().getClassLoader().getResourceAsStream("intros.properties");
		Properties newprops = new Properties();
		try {
			newprops.load(stream);
			props = newprops;
		} 
		catch (IOException e) {}
	}
	
	@Override
	public void parse(Message message, boolean isadmin) {
		this.m = message;
		this.admin = isadmin;

	}

}

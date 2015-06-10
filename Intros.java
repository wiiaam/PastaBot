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
				String target = "PRIVMSG " + m.getParam();
				if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
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
				else if(m.getBotParams().get(0).equals("view")){
					if(m.getBotParams().size() > 2){
						todo = "toomany";
					}
					else if(m.getBotParams().size() == 2){
						if(props.getProperty(m.getBotParams().get(1)) != null && !props.getProperty(m.getBotParams().get(1)).equals("")) todo = "viewother " + m.getBotParams().get(1);
						else todo = "nointroother";
						
					}
					else if(props.getProperty(m.getSender()) != null && !props.getProperty(m.getSender()).equals("")) todo = "view";
					else todo = "nointro";
				}
				
				
				try {
					FileOutputStream out = new FileOutputStream(new File(this.getClass().getResource("intros.properties").toURI()));
					props.store(out, "Intros");
					out.close();
				} 
				catch (IOException | URISyntaxException e) {}
				
				if(todo.equals("")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :Parameter not recognized. Usage: .intro <parameter> <intro> Parameters are set, del, view ";
					return toreturn;
				}
				if(todo.equals("nointro")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :You do not have an intro set";
					return toreturn;
				}
				if(todo.equals("nointroother")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :Intro not found";
					return toreturn;
				}
				if(todo.equals("toomany")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :Too many arguments. Usage: .intro view <user> ";
					return toreturn;
				}
				if(todo.equals("no")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :fk u";
					return toreturn;
				}
				if(todo.equals("view")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :" + props.getProperty(m.getSender());
					return toreturn;
				}
				if(todo.startsWith("viewother")){
					String[] toreturn = new String[1];
					toreturn[0] = target + " :" + props.getProperty(todo.split("\\s+")[1]);
					return toreturn;
				}
				else{
					String[] toreturn;
					toreturn = new String[1];
					toreturn[0] = target + " :Your intro has been " + todo;
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

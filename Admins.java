import java.io.*;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;


public class Admins implements Module{
	
	private File file;
	private HashSet<String> admins;
	private String command = "admin";
	private Message m;
	private boolean admin;
	
	public Admins(){
		try {
			this.file = new File(this.getClass().getResource("admins").toURI());
		} 
		catch (URISyntaxException e) {}
		admins = new HashSet<String>();
		Scanner scan;
		try {
			scan = new Scanner(file);
			while(scan.hasNext()){
				admins.add(scan.nextLine());
			}
		} 
		catch (FileNotFoundException e) {
		}
	}
	
	public void write(){
		try {
			PrintWriter writer = new PrintWriter(file);
			for(String s : admins){
				writer.println(s);
			}
			writer.close();
		} 
		catch (IOException e) {}
		
	}
	
	
	public boolean has(String username){
		return admins.contains(username);
	}
	
	public void add(String username){
		admins.add(username);
	}

	public String getCommand() {
		return command;
	}

	@Override
	public String[] outputs() {
		if(m.isBotCommand()){
			if(m.getBotCommand().equals(command)){
				String target = m.getParam();
				if(!target.startsWith("#")) target = m.getSender();
				boolean selfdelete = false;
				String todo = "";
				if(!admin){
					todo = "privilege";
				}
				else if(!m.hasBotParams()){}
				else if(m.getBotParams().get(0).equals("add")){
					todo = "added";
					for(int i = 1; i <  m.getBotParams().size(); i++){
						admins.add(m.getBotParams().get(i));
					}			
				}
				else if(m.getBotParams().get(0).equals("del")){
					todo = "deleted";
					for(int i = 1; i <  m.getBotParams().size(); i++){
						if(m.getBotParams().get(i).equals(m.getSender())){
							selfdelete = true;
							if(m.getBotParams().size() == 1){
								todo = "selfdelete";
							}
						}
						else admins.remove(m.getBotParams().get(i));
					}			
				}
				if(todo.equals("")){
					String[] toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :Parameter not recognized. Usage: .add <parameter> <admins> Parameters are add, del ";
					return toreturn;
				}
				else if(todo.equals("privilege")){
					String[] toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :" + m.getSender() + ": You are not admin, please check your privilege";
					return toreturn;
				}
				else{
					String[] toreturn;
					if(selfdelete){
						toreturn = new String[2];
						toreturn[1] = "PRIVMSG " + target + " :You cannot delete yourself from the admins list";
					}
					else toreturn = new String[1];
					toreturn[0] = "PRIVMSG " + target + " :The specified admins have been " + todo;
					write();
					return toreturn;
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

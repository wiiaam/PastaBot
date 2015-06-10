import java.io.*;
import java.net.*;
import java.util.*;

public class IrcBot {
	
	// Constants
	private String propertiesFile = "settings.properties";
	
	// Fields
	private Socket socket;
	private PrintStream serverout;
	private Scanner serverin;
	private PrintStream clientout = new PrintStream(System.out);
	private Scanner clientin = new Scanner(System.in);
	private Properties props = new Properties();
	private HashSet<Module> modules = new HashSet<Module>();
	private Admins admins;
	private String permaadmin = "";
	private boolean listening = false;
	private Ignores ignores;
	private String password;
	
	/**
	 * Creates a new IrcBot
	 */
	public IrcBot(){
		readConfig();
		permaadmin = props.getProperty("permaadmin");
		
		admins = new Admins();
		modules.add(admins);
		
		ignores = new Ignores();
		modules.add(ignores);
		
		modules.add(new Bots());
		//modules.add(new HtmlParser());
		modules.add(new Rules());
		modules.add(new Intros());
		modules.add(new Cucks());
		modules.add(new Quote());
		modules.add(new Help());
		modules.add(new Version());
		modules.add(new Administration());
		modules.add(new Triggers());
		modules.add(new Ping());
		modules.add(new Fortune());
		modules.add(new Rainbow());
		
		
		if(admins == null){
			clientout.println("Error reading admins file");
			return;
		}
		if(props.get("server") == null){
			clientout.println("Error, server not set");
			clientout.println("Please make sure an entry for server is in settings.properties");
		}
		else if(props.get("port") == null){
			clientout.println("Error, port not set");
			clientout.println("Please make sure an entry for port is in settings.properties");
		}
		else if(props.get("nick") == null){
			clientout.println("Error, nick not set");
			clientout.println("Please make sure an entry for nick is in settings.properties");
		}
		else if(props.get("user") == null){
			clientout.println("Error, user not set");
			clientout.println("Please make sure an entry for user is in settings.properties");
		}
		else{
			clientout.println("Config File Read");
			clientout.println("Enter login");
			while(true){
				if (clientin.hasNextLine()) {
					password = clientin.nextLine();
					break;
				}
			}
			connect();
		}
	}
	
	/**
	 * Connects the IrcBot to the socket
	 */
	public void connect(){
		String server = props.getProperty("server");
		String port = props.getProperty("port");
		try {
			clientout.println("Creating socket on " + props.getProperty("server") + ":" + props.getProperty("port"));
			socket = new Socket(server, Integer.parseInt(port));
			clientout.println("Creating streams");
			serverin = new Scanner(socket.getInputStream());
			serverout = new PrintStream(socket.getOutputStream());
			clientout.println("Sending login information");
			send("NICK " + props.getProperty("nick"));
			send("USER " + props.getProperty("user"));
			while(true){
				if(serverin.hasNextLine()){
					String line = serverin.nextLine();
					//clientout.println(line);
					Message m = new Message(line);
					if(m.getCommand().equals("002")) clientout.println("Connected");
					if(m.getCommand().equals("433")) clientout.println("Nick in use");
					if(m.getCommand().equals("451")) clientout.println("Register first");
					if(m.getCommand().equals("376")) break;
				}
			}
			clientout.println("Logged in successfully");
			send("PRIVMSG Nickserv :IDENTIFY " + password);
			new Thread(new Runnable(){
				public void run(){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					listenToServer();
				}
			}).start();
			new Thread(new Runnable() {
				public void run() {
					inputListener();
				}
			}).start();
			listening = true;
		} 
		catch (NumberFormatException | IOException e) {
		}
		
		
	}
	
	/**
	 * Reads the config file
	 */
	public void readConfig(){
		InputStream stream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
		Properties newprops = new Properties();
		try {
			newprops.load(stream);
			props = newprops;
		} 
		catch (IOException e) {
			clientout.println("Error reading settings.properties, does it exist?");
		}
	}
	
	public void send(String message){
		serverout.println(message + "\r\n");
		serverout.flush();
	}
	
	public void pm(String target, String message){
		send("PRIVMSG " + target + " :" + message);
	}
	
	private void listenToServer(){
		clientout.println("Listening to server");
		send("JOIN " + props.getProperty("channel"));
		while(true){
			if(serverin.hasNextLine()){
				String line = serverin.nextLine();
				clientout.println(line);
				Message m = new Message(line);
				clientout.println(m.getTrailing());
				final Message message = m;
				final boolean senderisadmin = (admins.has(message.getSender()) || permaadmin.equals(message.getSender()));
				if(ignores.has(m.getSender()) && !senderisadmin){
					clientout.println("ignoring message");
					continue;
				}
				for(final Module module : modules){
					new Thread(new Runnable(){
						public void run(){
							module.parse(message, senderisadmin);
							String[] outputs = module.outputs();
							if(outputs != null){
								for(int i = 0; i < outputs.length; i++){
									clientout.println(outputs[i]);
									send(outputs[i]);
									if(module.getClass().getName().equals("Admins")){
										admins = (Admins)module;
									}
								}
							}
						}
					}).start();
				}
			}
		}
	}
	
	
	/**
	 * Listens to the system in
	 */
	public void inputListener() {
		while (listening) {
			if (clientin.hasNextLine()) {
				String command = clientin.nextLine();
				String[] splitcom = command.split(" ");
				if (command.startsWith("/")) {
					if (splitcom[0].equals("/join")) {
						if (splitcom.length == 2) {
							send("JOIN " + splitcom[1]);
						} 
						else {
							clientout.println("Error, not enough parameters");
						}
					}
					else if (splitcom[0].equals("/leave")) {
						if (splitcom.length == 2) {
							send("PART " + splitcom[1]);
						} 
						else {
							clientout.println("Error, not enough parameters");
						}
					}
					else if (splitcom[0].equals("/raw")) {
						if (splitcom.length > 1) {
							String tosend = "";
							for(int i = 1; i < splitcom.length; i++){
								tosend += (splitcom[i] + " ");
							}
							send(tosend);
						} 
						else {
							clientout.println("Error, not enough parameters");
						}
					} 
					else {
						clientout.println("Error, command not recognized");
					}
				} 
				else if (command.startsWith("#")) {
					String message = "";
					for (int i = 1; i < splitcom.length; i++) {
						message += (splitcom[i] + " ");
					}
					pm(splitcom[0], message);
				}
			}
		}
	}
	
	
	
	public static void main(String[] args){
		new IrcBot();
	}
}

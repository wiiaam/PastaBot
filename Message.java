import java.util.ArrayList;


public class Message {
	
	private String commandChar = ".";
	private String sender = "";
	private String senderaddress  = "";
	private String command  = "";
	public String param  = "";
	private String trailing  = "";
	private boolean isbotcommand = false;
	private String botcommand  = "";
	private ArrayList<String> botparams = new ArrayList<String>();
	
	public Message(String message){
		String[] messageSplit = message.split("\\s+");
		/**
		System.out.println(messageSplit.length);
		for(int i = 0; i < messageSplit.length; i++){
			System.out.println(messageSplit[i]);
		}
		*/
		if(messageSplit.length == 2){
			sender = "";
			command = messageSplit[0];
			param = "";
			trailing = messageSplit[1].substring(1);
			for(int i = 2; i < messageSplit.length; i++){
				trailing += " " + messageSplit[i];
			}
		}
		else if(messageSplit.length == 3){
			sender = messageSplit[0].substring(1);
			command = messageSplit[1];
			param = "";
			trailing = messageSplit[2].substring(1);
			for(int i = 3; i < messageSplit.length; i++){
				trailing += " " + messageSplit[i];
			}
		}
		else{
			sender = messageSplit[0].substring(1);
			command = messageSplit[1];
			param = messageSplit[2];
			trailing = messageSplit[3].substring(1);
			for(int i = 4; i < messageSplit.length; i++){
				trailing += " " + messageSplit[i];
			}
			if(trailing.startsWith(commandChar) && command.equals("PRIVMSG")){
				isbotcommand = true;
				String[] trailingSplit = trailing.split("\\s+");
				botcommand = trailingSplit[0].substring(commandChar.length());
				for(int i = 1;  i < trailingSplit.length; i++){
					botparams.add(trailingSplit[i]);
				}
			}
		}
		String[] sendersplit = sender.split("!");
		if(sendersplit.length > 1){
			sender = sendersplit[0];
			senderaddress = sendersplit[1];
		}
		else{
			senderaddress = sender;
		}
	}
	
	public boolean hasBotParams(){
		if(botparams.size() == 0) return false;
		return true;
	}
	
	public String getSender(){
		return sender;
	}
	
	public String getSenderAddress(){
		return senderaddress;
	}
	
	public String getCommand(){
		return command;
		
	}
	
	public String getParam(){
		return param;
	}
	
	public String getTrailing(){
		return trailing;
	}
	
	public boolean isBotCommand(){
		return isbotcommand;
	}
	
	public String getBotCommand(){
		return botcommand;
	}
	
	public ArrayList<String> getBotParams(){
		return botparams;
	}
}

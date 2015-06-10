import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class HtmlParser implements Module{
	
	private String command = "toggletitles";
	private Message m;
	private boolean admin;
	private boolean on = true;
	
	public void parse(Message m, boolean admin){
		this.m = m;
		this.admin = admin;
	}
	
	public String[] outputs(){
		String target = "PRIVMSG " + m.getParam();
		if(!target.startsWith("#")) target = "NOTICE " + m.getSender();
		String[] messageSplit = m.getTrailing().split("\\s+");
		if(m.isBotCommand() && admin){
			if(m.getBotCommand().equals(command)){
				String[] outputs = new String[1];
				if(on) outputs[0] = target + " :Title reporting is now off";
				else outputs[0] = target + " :Title reporting is now on";
				on = !on;
				return outputs;
			}
		}
		if(!on)return null;
		for(int i = 0; i < messageSplit.length; i++){
			if(messageSplit[i].startsWith("http://") || messageSplit[i].startsWith("https://")){
				try {
					URL url = new URL(messageSplit[i]);
					Scanner scan = new Scanner(url.openStream());
					String title = "";
					boolean titlefound = false;
					while(scan.hasNext()){
						String next = scan.next();
						if(next.contains("<title>")){
							titlefound = true;
							next = next.split("<title")[1];
						}
						else if(next.contains("<title")){
							titlefound = true;
							next = next.split("<title")[1];
							/*
							while(scan.hasNext()){
								next = scan.next();
								if(next.contains(">")){
									next = next.split(">")[1];
									break;
								}
								
							}
							*/
						}
						if(titlefound){
							if(next.contains("</title>")){
								next = next.split("</title>")[0];
								title += next + " ";
								break;
							}
							else title += next + " ";
							if(title.split("\\s+").length > 20){
								title += "...";
								break;
							}
						}
					}
					if(title.length() > 100){
						title = title.substring(0, 95) + "...";
					}
					String[] outputs = new String[1];
					title = title.substring(1);
					outputs[0] = (target + " :[URL] " + title + "(" + url.getHost() + ")");
					return outputs;
				}
				catch(Exception e){
					String[] outputs = new String[1];
					e.printStackTrace();
					outputs[0] = (target + " :Could not read url, " + e);
					return outputs;
				}
			}
		}
		return null;
	}

}

import java.io.PrintStream;
import java.util.HashSet;


public class Voting implements Module {
	
	private boolean isvoting = false;
	private HashSet<String> voted = new HashSet<String>();
	private Message m;
	private boolean admin;
	private double yes = 0;
	private double votes = 0;
	private String voteroom;
	private String topic;
	private PrintStream out;
	private long lastvote = System.currentTimeMillis();
	
	@Override
	public String[] outputs() {
		if((lastvote + 1000*60) < System.currentTimeMillis()) {
			isvoting = false;
			voted.clear();
		}
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		String[] outputs = new String[1];
		if(m.getBotCommand().equals("vote")){
			if(!isvoting){
				isvoting = true;
				voteroom = target;
				startVote();
				topic = m.getBotParamsString();
				outputs[0] = target + " :A vote has been started! Voting will last 60 seconds. The topic is: \"" + topic + "\". Vote with .voteyes and .voteno";
				lastvote = System.currentTimeMillis();
				return outputs;
			}
			else{
				outputs[0] = target + " :A vote for \"" + topic + "\" is already in progress";
				return outputs;
			}
		}
		if(m.getBotCommand().equals("voteyes") || m.getBotCommand().equals("voteno")){
			if(isvoting){
				if(!voted.contains(m.getSender())){
					voted.add(m.getSender());
					if(m.getBotCommand().equals("voteyes")){
						yes++;
						votes++;
						outputs[0] = target + " :" + m.getSender() + ": You have voted yes";
						return outputs;
					}
					if(m.getBotCommand().equals("voteno")){
						votes++;
						outputs[0] = target + " :" + m.getSender() + ": You have voted no";
						return outputs;
					}
				}
				else{
					outputs[0] = target + " :" + m.getSender() + ": You have already voted";
					return outputs;
				}
			}
			else{
				outputs[0] = target + " :" + m.getSender() + ": There is currently no vote being run";
				return outputs;
			}
		}
		return null;
	}
	
	private void startVote(){
		new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(1000*30);
					out.println(voteroom + " :The vote will end in 30 seconds\r\n");
					Thread.sleep(1000*20);
					out.println(voteroom + " :The vote will end in 10 seconds\r\n");
					Thread.sleep(1000*10);
				} 
				catch (InterruptedException e) {}
				out.println(results() + "\r\n");
			}
		}).start();
	}
	
	@Override
	public void parse(Message message, boolean isadmin) {
		m = message;
		admin = isadmin;
	}
	
	public String results(){
		System.out.println("checking results!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(votes == 0) return voteroom + " :Voting finished! No votes were sent";
		double percentyes = (yes/votes*100);
		
		double percentno = 100 - percentyes;
		String result = voteroom + String.format(" :Voting finished! Results were %.0f yes (%.0f%%), %.0f no (%.0f%%) out of %.0f votes" , yes, percentyes, (votes-yes), percentno, votes) ;
		yes = 0;
		votes = 0;
		return result;
	}
	public void giveStream(PrintStream ps){
		out = ps;
	}
}

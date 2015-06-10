
public class Fortune implements Module {
	
	private Message m;
	private boolean admin;
	private String[] fortunes;
	private String[] eightball;
	
	public Fortune(){
		String[] eightball = {"As I See It Yes", "Ask Again Later", "Better Not Tell You Now", "Cannot Predict Now",
				"Concentrate and Ask Again", "Don't Count On It", "It Is Certain", "It Is Decidely So", 
				"Most Likely", "My Reply Is No", "My Sources Say No", "Outlook Good", "Outlook Not So Good", 
				"Reply Hazy Try Again", "Signs Point to Yes", "Very Doubtful", "Without A Doubt", "Yes", 
				"Yes - Definitely", "You May Rely On It"};
		this.eightball = eightball;
		
		String[] fortunes = {"13Your fortune: Reply hazy, try again", "4Your fortune: Excellent Luck", 
				"7Your fortune: Good Luck", "3Your fortune: Average Luck", "9Your fortune: Bad Luck",
				"9Your fortune: Good news will come to you by mail", "9Your fortune: （　´_ゝ`）ﾌｰﾝ ",
				"10Your fortune: ｷﾀ━━━━━━(ﾟ∀ﾟ)━━━━━━ !!!!", "12Your fortune: You will meet a dark handsome stranger",
				"2Your fortune: Better not tell you now", "2Your fortune: Outlook good", "6Your fortune: Very Bad Luck",
				"13Your fortune: Godly Luck"};
		this.fortunes = fortunes;
	}
	
	@Override
	public String[] outputs() {
		String[] outputs = new String[1];
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		
		if(m.getBotCommand().equals("8ball")){
			outputs[0] = target + " :" + eightball[(int)(Math.floor(Math.random() * eightball.length))];
			return outputs;
		}
		if(m.getBotCommand().equals("fortune")){
			outputs[0] = target + " :" + fortunes[(int)(Math.floor(Math.random() * fortunes.length))];
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

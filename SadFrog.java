
public class SadFrog implements Module {

	private Message m;
	private boolean admin;
	
	@Override
	public String[] outputs() {
		String target = "PRIVMSG " + m.getParam();
		if(!m.getParam().startsWith("#")) target = "NOTICE " + m.getSender();
		if(m.getBotCommand().equals("sadfrog")){
			if(admin){
				String[] outputs = new String[37];
				outputs[0] = target + "  :▁▁▁▁▁▁▁▁▁▁▁████████     ██████";
				outputs[1] = target + "  :▁▁▁▁▁▁▁▁▁▁█░░░░░░░░██  ██░░░░░░█";
				outputs[2] = target + "  :▁▁▁▁▁▁▁▁▁█░░░░░░░░░░█░░░░░░░░░█";
				outputs[3] = target + "  :▁▁▁▁▁▁▁▁█░░░░░░███░░░█░░░░░░░░░█";
				outputs[4] = target + "  :▁▁▁▁▁▁▁█░░░███░░░███░█░░░░████░█";
				outputs[5] = target + "  :▁▁▁▁▁▁▁█░██░░░░░░░░███░░██░░░░███";
				outputs[6] = target + "  :▁▁▁▁▁▁█░░░░░░░░░░░░░░░░░█░░░░░░░░███";
				outputs[7] = target + "  :▁▁▁▁▁█░░░░░░░░░░░░░██████░░░░░████░░█";
				outputs[8] = target + "  :▁▁▁▁▁█░░░░░░░░░░█████░░████░░██░░██░░█";
				outputs[9] = target + "  :▁▁▁██░░░░░░░░███░░░░░░░░░░█░░░░░░░░███";
				outputs[10] = target + " :▁▁▁█░░░░░░░░░░░░░░█████████░░░█████████";
				outputs[11] = target + " :▁▁█░░░░░░░░░░█████ ████   ████ █████ █";
				outputs[12] = target + " :▁█░░░░░░░░░░█     █ ███ █    ███  █ █";
				outputs[13] = target + " :█░░░░░░░░░░░░█   ████ ████  ██ ██████";
				outputs[14] = target + " :░░░░░░░░░░░░░█████████░░░████████░░░█";
				outputs[15] = target + " :░░░░░░░░░░░░░░░░█░░░░░█░░░░░░░░░░░░█";
				outputs[16] = target + " :░░░░░░░░░░░░░░░░░░░░██░░░░█░░░░░░██";
				outputs[17] = target + " :░░░░░░░░░░░░░░░░░░██░░░░░░░███████";
				outputs[18] = target + " :░░░░░░░░░░░░░░░░██░░░░░░░░░░█░░░░░█              feels";
				outputs[19] = target + " :░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█                bad";
				outputs[20] = target + " :░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█            man";
				outputs[21] = target + " :░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█";
				outputs[22] = target + " :░░░░░░░░░░░█████████░░░░░░░░░░░░░░██";
				outputs[23] = target + " :░░░░░░░░░░█▒▒▒▒▒▒▒▒███████████████▒▒█";
				outputs[24] = target + " :░░░░░░░░░█▒▒███████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█";
				outputs[25] = target + " :░░░░░░░░░█▒▒▒▒▒▒▒▒▒█████████████████";
				outputs[26] = target + " :░░░░░░░░░░████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█";
				outputs[27] = target + " :░░░░░░░░░░░░░░░░░░██████████████████";
				outputs[28] = target + " :░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█";
				outputs[29] = target + " :██░░░░░░░░░░░░░░░░░░░░░░░░░░░██";
				outputs[30] = target + " :▓██░░░░░░░░░░░░░░░░░░░░░░░░██";
				outputs[31] = target + " :▓▓▓███░░░░░░░░░░░░░░░░░░░░█";
				outputs[32] = target + " :▓▓▓▓▓▓███░░░░░░░░░░░░░░░██";
				outputs[33] = target + " :▓▓▓▓▓▓▓▓▓███████████████▓▓█";
				outputs[34] = target + " :▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓██";
				outputs[35] = target + " :▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█";
				outputs[36] = target + " :▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█";
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



/**
 * Interface for IrcBot modules
 * 
 * 
 * How modules work:
 * 
 * The module does not need to have a constructor.
 * 
 * Modules are given a Message and a boolean to say if the sender is an admin.
 * This is done with Module.parse(message, isadmin).
 * 
 * Then the IrcBot will request all outputs from the module.
 * This is done with Module.outputs().
 * If the module does not have any outputs (i.e, if the bot command returned by Message.getBotCommand() 
 * was not the same as the module's bot command) then it will return null.
 * 
 * Right now, all modules need to me manually constructed and added to the modules set in IrcBot.
 * A feature to eliminate this will be added later.
 * 
 * 
 * @author wiiam
 *
 */
public interface Module {
	
	/**
	 * Returns all outputs that are to be sent as a string array
	 */
	public String[] outputs();
	
	/**
	 * Takes the string that is send from the server, and parses it.
	 */
	public void parse(Message message, boolean isadmin);
	//public HashSet<String> getSet();
}

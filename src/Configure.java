import akka.actor.ActorRef;

/**
 * Message: scan configuration for a single file.
 * 
 * @author Brian To
 * @author Matthew Metcalf
 */
public class Configure {
	private final String filepath;
	private final ActorRef collection;
	private final String regex;
	
	/**
	 * Constructor 
	 * 
	 * @param filepath path to file
	 * @param collection reference to a {@link CollectionActor}
	 * @param regex regex to match all lines in the file
	 */
	public Configure(String filepath, ActorRef collection, String regex) {
		this.filepath = filepath;
		this.collection = collection;
		this.regex = regex;
	}
	
	public String getFilepath() {
		return this.filepath;
	}

	public ActorRef getCollectionActor() {
		return this.collection;
	}

	public String getRegex() {
		return regex;
	}
}

import akka.actor.ActorRef;

public class Configure {
	private final String filepath;
	private final ActorRef collection;
	private final String regex;
	
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

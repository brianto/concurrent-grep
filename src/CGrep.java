import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Actors;

/**
 * Entry class.
 * 
 * @author Brian
 */
public class CGrep {

	/**
	 * Main method.
	 * 
	 * Arguments are in the format <code>pattern [files.. | -]</code> where
	 * files is a list of files to grep through and - is "read from stdout".
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		String pattern = args[0];

		List<String> files = new ArrayList<String>(Arrays.asList(args));
		files.remove(0);

		FileCount filecount = new FileCount(files.size());

		// Create a CollectionActor, start it
		ActorRef collectionActor = Actors.actorOf(CollectionActor.class)
				.start();
		
		// Send the FileCount message to it.
		collectionActor.tell(filecount);

		// Create a configured ScanActor for every file
		List<ActorRef> scanActors = new ArrayList<ActorRef>();
		for (String file : files) {
			ActorRef scanActor = Actors.actorOf(ScanActor.class).start();

			Configure config = new Configure(file, collectionActor, pattern);

			scanActors.add(scanActor);
			scanActor.tell(config); // Configure actor and start crunching
		}
	}
}

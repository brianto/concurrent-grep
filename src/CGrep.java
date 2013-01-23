import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Actors;

public class CGrep {
	public static void main(String[] args) {
		String pattern = args[0];

		List<String> files = new ArrayList<String>(Arrays.asList(args));
		files.remove(0);

		FileCount filecount = new FileCount(files.size());

		// Create a CollectionActor, start it, and send the FileCount message to
		// it.
		ActorRef collectionActor = Actors.actorOf(CollectionActor.class)
				.start();
		collectionActor.tell(filecount);

		List<ActorRef> scanActors = new ArrayList<ActorRef>();
		for (String file : files) {
			ActorRef scanActor = Actors.actorOf(ScanActor.class).start();

			Configure config = new Configure(file, collectionActor,pattern);

			scanActors.add(scanActor);
			scanActor.tell(config);
		}
	}
}
